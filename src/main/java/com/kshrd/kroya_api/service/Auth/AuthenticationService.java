package com.kshrd.kroya_api.service.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.kroya_api.config.JwtService;
import com.kshrd.kroya_api.entity.CodeEntity;
import com.kshrd.kroya_api.entity.UserEntity;
import com.kshrd.kroya_api.entity.token.Token;
import com.kshrd.kroya_api.entity.token.TokenRepository;
import com.kshrd.kroya_api.enums.ResponseMessage;
import com.kshrd.kroya_api.enums.TokenType;
import com.kshrd.kroya_api.exception.BadRequestException;
import com.kshrd.kroya_api.exception.CustomExceptionSecurity;
import com.kshrd.kroya_api.exception.NotFoundExceptionHandler;
import com.kshrd.kroya_api.exception.exceptionValidateInput.Validation;
import com.kshrd.kroya_api.payload.Auth.*;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.repository.Code.CodeRepository;
import com.kshrd.kroya_api.repository.User.UserRepository;
import com.kshrd.kroya_api.service.Code.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final Validation validation;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final CodeRepository codeRepository;
    private final EmailService emailService;

    // Step 1: Validate Email (for the first screen)
    public BaseResponse<?> checkEmailExist(String email) {

        log.debug("🔍 Checking email for validation: {}", email);

        // Validate the email format
        validation.ValidationEmail(email);

        // Retrieve user entity by email
        var userEntity = userRepository.findByEmail(email);

        // Check if user account exists
        if (userEntity == null) {
            log.warn("❌ No account associated with email: {}", email);
            throw new NotFoundExceptionHandler("💔 Oops! No account associated with this email. Please send OTP and verify OTP before registration.");
        }

        log.info("✅ Email found for: {}", email);

        // Prepare successful response
        return BaseResponse.builder()
                .payload(userEntity.getEmail())
                .message("🎉 Email found! You can now proceed to enter your password. 🗝️")
                .statusCode("200")
                .build();
    }

    // Step 2: Authenticate Email and Password (for the second screen)
    public BaseResponse<?> loginByEmailAndPassword(LoginRequest loginRequest) {

        log.debug("Validating login request for email: {}", loginRequest.getEmail());
        validation.ValidationEmail(loginRequest.getEmail());

        var userEntity = userRepository.findByEmail(loginRequest.getEmail());

        if (userEntity == null) {
            log.warn("User not found for email: {}", loginRequest.getEmail());
            throw new NotFoundExceptionHandler("User not found");
        }

        if (!passwordEncoder.matches(loginRequest.getPassword(), userEntity.getPassword())) {
            log.warn("Incorrect password for email: {}", loginRequest.getEmail());
            throw new CustomExceptionSecurity(ResponseMessage.INCORRECT_PASSWORD);
        }

        log.info("User authenticated successfully for email: {}", loginRequest.getEmail());
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setEmail(loginRequest.getEmail());
        authenticationRequest.setPassword(loginRequest.getPassword());

        return authenticate(authenticationRequest);
    }

    // Helper method to authenticate and generate tokens
    private BaseResponse<?> authenticate(AuthenticationRequest request) {

        log.debug("Authenticating user: {}", request.getEmail());
        var user = userRepository.findByEmail(request.getEmail());

        if (user == null) {
            log.warn("User not found during authentication for email: {}", request.getEmail());
            throw new CustomExceptionSecurity(ResponseMessage.INCORRECT_USERNAME);
        }

        // Authenticate using Spring Security's authentication manager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        log.info("Authentication successful for user: {}", request.getEmail());

        // Generate JWT tokens
        String jwtToken = jwtService.generateToken(user); // Using user for both UserDetails and UserEntity

        var refreshToken = jwtService.generateRefreshToken(user);

        // Ensure isDeleted is set to false
        user.setDeleted(false);
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        log.info("Generated new JWT tokens for user: {}", request.getEmail());

        return BaseResponse.builder()
                .payload(AuthenticationResponse.builder()
                        .role(user.getRole())
                        .isDeleted(user.isDeleted())
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .profileImage(user.getProfileImage())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .createdDate(user.getCreatedAt().toString())
                        .build())
                .build();
    }

    // Helper method to save the user's token to the database for later use
    private void saveUserToken(UserEntity user, String jwtToken) {
        log.debug("Saving token for user: {}", user.getEmail());

        // Check if the token already exists
        Optional<Token> existingToken = tokenRepository.findByToken(jwtToken);

        if (existingToken.isPresent()) {
            // Update the existing token
            Token token = existingToken.get();
            token.setTokenExpired(false);
            token.setTokenRevoked(false);
            tokenRepository.save(token);
        } else {
            // Insert a new token
            var token = Token.builder()
                    .user(user)
                    .token(jwtToken)
                    .tokenType(TokenType.BEARER)
                    .tokenExpired(false)
                    .tokenRevoked(false)
                    .build();
            tokenRepository.save(token);
        }
    }

    // Helper method to revoke all tokens for a user (used when a user logs in)
    private void revokeAllUserTokens(UserEntity user) {
        log.debug("Revoking all valid tokens for user: {}", user.getEmail());
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            log.debug("No valid tokens found for user: {}", user.getEmail());
            return;
        }
        validUserTokens.forEach(token -> {
            token.setTokenExpired(true);
            token.setTokenRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
        log.info("All tokens revoked for user: {}", user.getEmail());
    }

    public BaseResponse<RefreshTokenResponse> refreshToken(RefreshTokenRequest refreshTokenRequest) {
        final String refreshToken = refreshTokenRequest.getRefreshToken();
        final String userEmail;

        // Check if the refresh token is missing
        if (refreshToken == null || refreshToken.isEmpty()) {
            log.warn("Missing refresh token in request body");
            return BaseResponse.<RefreshTokenResponse>builder()
                    .statusCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))
                    .message("Missing refresh token in request body")
                    .build();
        }

        log.debug("Refresh token: {}", refreshToken);

        // Extract the user email from the refresh token
        userEmail = jwtService.extractUsername(refreshToken);
        log.debug("Extracted user email: {}", userEmail);

        if (userEmail != null) {
            // Find the user by email
            var user = this.userRepository.findByEmail(userEmail);

            // Validate the refresh token and generate new tokens
            if (user != null && jwtService.isTokenValid(refreshToken, user)) {
                // Generate a new access token
                var newAccessToken = jwtService.generateToken(user);

                // Generate a new refresh token
                var newRefreshToken = jwtService.generateRefreshToken(user);

                log.info("Successfully generated new access and refresh tokens for user: {}", userEmail);

                // Revoke all existing tokens for the user
                revokeAllUserTokens(user);

                // Save the new access token to the database
                saveUserToken(user, newAccessToken);

                // Build the response with the new tokens
                var authResponse = RefreshTokenResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(newRefreshToken)
                        .build();

                // Return the new tokens in the response
                return BaseResponse.<RefreshTokenResponse>builder()
                        .statusCode(String.valueOf(HttpServletResponse.SC_OK))
                        .message("New access and refresh tokens generated successfully")
                        .payload(authResponse)
                        .build();
            } else {
                log.warn("Invalid token or user not found for email: {}", userEmail);
                return BaseResponse.<RefreshTokenResponse>builder()
                        .statusCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))
                        .message("Invalid token or user not found")
                        .build();
            }
        } else {
            log.error("Failed to extract user information from token");
            return BaseResponse.<RefreshTokenResponse>builder()
                    .statusCode(String.valueOf(HttpServletResponse.SC_UNAUTHORIZED))
                    .message("Invalid token: No user information")
                    .build();
        }
    }

    public BaseResponse<?> generateOtp(String email) throws MessagingException {

        // Validate that email is not null or empty
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must not be empty.");
        }

        // Validate the email format
        validation.ValidationEmail(email);

        // Find user by email
        var user = userRepository.findByEmail(email);

        // Check if OTP already exists for this email
        CodeEntity codeEntity = codeRepository.findByEmail(email);

        // Generate a 6-digit OTP code
        String otp = String.format("%06d", new Random().nextInt(999999));

        // Set OTP expiry to 5 minutes from now
        int otpExpiryMinutes = 3;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusMinutes(otpExpiryMinutes);

        // If OTP already exists, update it; otherwise, create a new CodeEntity
        if (codeEntity != null) {
            log.info("Updating existing OTP for email: {}", email);
            codeEntity.setPinCode(otp);
            codeEntity.setExpireDate(expiryDate);
            codeEntity.setCreateDate(now);
        } else {
            log.info("Creating new OTP for email: {}", email);
            codeEntity = new CodeEntity();
            codeEntity.setEmail(email);
            codeEntity.setPinCode(otp);
            codeEntity.setCreateDate(now);
            codeEntity.setExpireDate(expiryDate);
            codeEntity.setUser(user);
        }

        // Attempt to save OTP code to the database
        try {
            codeRepository.save(codeEntity);
        } catch (Exception e) {
            log.error("Failed to save OTP for email: {}", email, e);
            throw new RuntimeException("An error occurred while saving OTP. Please try again.");
        }

        log.info("Generated OTP for email: {}, OTP: {}", email, otp);

        // Send the OTP to the user via email
        try {
            emailService.sendEmail(email, otp);
        } catch (MessagingException e) {
            log.error("Failed to send OTP email to: {}", email, e);
            throw new MessagingException("Failed to send OTP. Please check your email address.");
        }

        // Prepare response payload
        Map<String, String> payload = new LinkedHashMap<>();
        payload.put("email", email);
        payload.put("otp", otp);

        // Return success response
        return BaseResponse.builder()
                .payload(payload)
                .message("OTP generated and sent successfully. It will expire in " + otpExpiryMinutes + " minutes.")
                .statusCode("200")
                .build();
    }

    public BaseResponse<?> validateOtp(String email, String otp) {

        validation.ValidationEmail(email);

        // Validate email and OTP inputs
        log.info("Starting OTP validation for email: {}", email);
        if (email == null || email.isEmpty()) {
            log.warn("Email is empty or null for OTP validation.");
            throw new IllegalArgumentException("Email must not be empty.");
        }
        if (otp == null || otp.isEmpty()) {
            log.warn("OTP is empty or null for OTP validation.");
            throw new IllegalArgumentException("OTP must not be empty.");
        }

        if (!otp.matches("^[0-9]{6}$")) {
            log.warn("Invalid OTP format provided: {}", otp);
            throw new BadRequestException("Invalid OTP format [6 digits only].", new BadCredentialsException("Invalid OTP"));
        }

        // Find the OTP code by email
        var codeEntity = codeRepository.findByEmail(email);
        log.info("Fetched OTP entity for email: {}", email);
        if (codeEntity == null) {
            log.warn("OTP not found for email: {}", email);
            throw new NotFoundExceptionHandler("OTP not found for the provided email.");
        }

        // Check if the OTP matches
        if (!codeEntity.getPinCode().equals(otp)) {
            log.warn("Invalid OTP provided for email: {}. Expected: {}, Provided: {}", email, codeEntity.getPinCode(), otp);
            throw new BadRequestException("Invalid OTP provided.", new BadCredentialsException("Invalid OTP"));
        }

        // Check if the OTP has expired
        if (LocalDateTime.now().isAfter(codeEntity.getExpireDate())) {
            log.warn("OTP expired for email: {}. Expiry time: {}", email, codeEntity.getExpireDate());
            throw new BadRequestException("OTP has expired.", new BadCredentialsException("OTP has expired"));
        }

        // Check if user already exists with the provided email
        UserEntity existingUser = userRepository.findByEmail(email);
        if (existingUser != null) {
            // If user exists, just update the email verified status
            existingUser.setEmailVerified(true);
            existingUser.setEmailVerifiedAt(LocalDateTime.now());
            userRepository.save(existingUser);
            log.info("Existing user email verified for email: {}", email);
        } else {
            // If user does not exist, create a new user
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setPassword(passwordEncoder.encode("default_password"));
            newUser.setFullName("");
            newUser.setPhoneNumber("");
            newUser.setEmailVerified(true);
            newUser.setEmailVerifiedAt(LocalDateTime.now());
            newUser.setCreatedAt(LocalDateTime.now());
            newUser.setRole("ROLE_USER");

            userRepository.save(newUser);
            log.info("New user created and email verified for email: {}", email);
        }

        // Prepare success payload
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", email);
        payload.put("isEmailVerified", true);
        payload.put("verifiedAt", LocalDateTime.now());

        log.info("OTP validated and email verification completed for email: {}", email);

        return BaseResponse.builder()
                .message("OTP validated and email verified successfully")
                .statusCode("200")
                .payload(payload)
                .build();
    }

    public BaseResponse<?> register(PasswordRequest passwordRequest) {

        log.debug("Create password for email: {}", passwordRequest.getEmail());

        // Validate email and password fields
        if (passwordRequest.getEmail() == null || passwordRequest.getEmail().isEmpty()) {
            throw new BadRequestException("Email is required", new BadCredentialsException("Invalid email input"));
        }
        if (passwordRequest.getNewPassword() == null || passwordRequest.getNewPassword().isEmpty()) {
            throw new BadRequestException("Password is required", new BadCredentialsException("Invalid password input"));
        }

        // Find user by email
        var user = userRepository.findByEmail(passwordRequest.getEmail());
        if (user == null) {
            throw new NotFoundExceptionHandler("Email verification required: send and validate the OTP before registering");
        }

        // Check if the email has been verified via OTP before registration
        if (!user.isEmailVerified()) {
            throw new BadRequestException("Email not verified. Please validate OTP before registration.",
                    new BadCredentialsException("Email not verified"));
        }

        // Update the user's password
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);

        log.info("Password created successfully for email: {}", passwordRequest.getEmail());

        // Generate JWT tokens
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Revoke any existing tokens and save the new JWT token for the user
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        log.info("Generated JWT token for email: {}", passwordRequest.getEmail());

        // Build and return the response with user details
        return BaseResponse.builder()
                .message("Password created successfully")
                .statusCode("200")
                .payload(AuthenticationResponse.builder()
                        .role(user.getRole())
                        .accessToken(jwtToken)
                        .refreshToken(refreshToken)
                        .profileImage(user.getProfileImage())
                        .fullName(user.getFullName())
                        .email(user.getEmail())
                        .createdDate(user.getCreatedAt() != null ? user.getCreatedAt().toString() : "")
                        .build())
                .build();
    }


    public BaseResponse<?> saveUserInfo(UserInfoRequest userInfoRequest) {

        // Get the currently authenticated user
        UserEntity currentUser = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        log.info("User authenticated: {}", currentUser.getEmail());

        // Find user by email
        var user = userRepository.findByEmail(currentUser.getEmail());
        if (user == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        // Update the user's additional information only if provided
        if (userInfoRequest.getUserName() != null && !userInfoRequest.getUserName().isEmpty()) {
            user.setFullName(userInfoRequest.getUserName());
        }

        if (userInfoRequest.getPhoneNumber() != null && !userInfoRequest.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(userInfoRequest.getPhoneNumber());
        }

        // Save the updated user entity
        userRepository.save(user);

        // Prepare the payload with updated user info
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("email", user.getEmail());
        payload.put("fullName", user.getFullName());
        payload.put("phoneNumber", user.getPhoneNumber());

        return BaseResponse.builder()
                .message("User information saved successfully")
                .statusCode("200")
                .payload(payload)
                .build();
    }

    public BaseResponse<?> resetPassword(PasswordRequest passwordRequest) {

        log.info("Processing reset password for email: {}", passwordRequest.getEmail());

        // Find the user by email
        var user = userRepository.findByEmail(passwordRequest.getEmail());
        if (user == null) {
            throw new NotFoundExceptionHandler("User not found");
        }

        // Update the password for the user
        user.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        userRepository.save(user);

        log.info("Password reset successfully for email: {}", passwordRequest.getEmail());

        return BaseResponse.builder()
                .message("Password reset successfully")
                .statusCode("200")
                .build();
    }

}
