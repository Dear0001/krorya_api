package com.kshrd.kroya_api.controller;

import com.kshrd.kroya_api.payload.Auth.*;
import com.kshrd.kroya_api.payload.BaseResponse;
import com.kshrd.kroya_api.service.Auth.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/auth")
@AllArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "https://krorya-eosin.vercel.app"})
public class AuthController {

    private final AuthenticationService authenticationService;


    @Operation(
            summary = "🔄 Refresh Access Token",
            description = """
                    This endpoint allows users to obtain a new access token using a valid refresh token.\s
                    Make sure to include the refresh token in the Authorization header! 🛡️
                   \s
                    **📩 Response Summary**:
                    - **200**: ✅ New access token generated successfully!
                    - **401**: 🚫 Unauthorized. This may occur due to:
                        - Missing or invalid Authorization header
                        - Invalid token or user not found
                   \s"""
    )
    @PostMapping("/refresh-token")
    public BaseResponse<RefreshTokenResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authenticationService.refreshToken(refreshTokenRequest);
    }


    @Operation(
            summary = "📧 Email Availability Check",
            description = """
                    💌 This endpoint helps you find out if an email \
                    is registered in our system. Use this on the first screen to guide \
                    your users on whether to enter their password or go through OTP verification! 🎀
                    
                    **📩 Response Summary**:
                    - **200**: 🎉 Email is registered; users can proceed to enter their password! 🗝️
                    - **404**: 😢 No account found; please prompt users to send OTP and verify it before registration! 📧
                    - **400**: 🚫 Invalid email format; please check the email and try again! 📭
                    - **500**: ⚠️ An internal error occurred during OTP saving or sending the email. 🚨
                    """
    )
    @GetMapping("/check-email-exist")
    public BaseResponse<?> checkEmailExist(
            @Parameter(description = "📧 Please enter the email address to check for existence", required = true)
            @RequestParam String email) {
        return authenticationService.checkEmailExist(email);
    }


    @Operation(
            summary = "🔑 User Login with Email and Password 📧",
            description = """
                    This endpoint allows users to log in using their email and password.
                    Upon successful authentication, it returns JWT tokens for secure access! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Successfully authenticated the user and generated JWT tokens!
                    - **400**: 🚫 Invalid email format. Please provide a valid email address.
                    - **404**: ❌ User not found for the provided email.
                    - **403**: ❌ Incorrect password provided.
                    """
    )
    @PostMapping("/login")
    public BaseResponse<?> loginByEmailAndPassword(@RequestBody LoginRequest loginRequest) {
        return authenticationService.loginByEmailAndPassword(loginRequest);
    }


    @Operation(
            summary = "📧✨ Send OTP for Email Verification 🔑",
            description = """
                    This endpoint sends a One-Time Password (OTP) to the specified email address.
                    Use this to verify the user's email and ensure secure access! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Successfully sent the OTP.
                    - **400**: 🚫 Invalid email format or empty email.
                    - **500**: ⚠️ An internal error occurred during OTP saving or sending the email. 🚨
                    """
    )
    @PostMapping("/send-otp")
    public BaseResponse<?> sendOtp(
            @Parameter(description = "📧 Enter the email address to receive the OTP", required = true)
            @RequestParam String email) throws MessagingException {

        return authenticationService.generateOtp(email);
    }


    // Step 2: Validate OTP for Email Verification
    @Operation(
            summary = "🔑 Validate OTP for Email Verification 📧",
            description = """
                    This endpoint verifies the One-Time Password (OTP) sent to the user's email. 
                    Use this to complete the email verification process! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Successfully validated the OTP and verified the email!
                    - **400**: 🚫 Invalid request. This may occur due to:
                        - Empty email or OTP
                        - Invalid OTP format (must be 6 digits)
                        - Incorrect OTP provided
                        - OTP has expired
                    - **500**: ⚠️ An internal error occurred. Please try again.
                    """
    )
    @PostMapping("/validate-otp")
    public BaseResponse<?> validateOtp(
            @Parameter(description = "📧 Enter the email address for verification", required = true)
            @RequestParam String email,

            @Parameter(description = "🔑 Enter the OTP sent to your email", required = true)
            @RequestParam String otp) {

        return authenticationService.validateOtp(email, otp);
    }


    @Operation(
            summary = "🔑 Create Password for Registration 📧",
            description = """
                    This endpoint creates a password for a user during the registration process.
                    Ensure the email is verified via OTP before using this endpoint! 🎉
                    Upon successful authentication, it returns JWT tokens for secure access! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Password created successfully and generated JWT tokens!
                    - **400**: 🚫 Invalid request. This may occur due to:
                        - Empty email
                        - Empty password
                    - **404**: ❌ Email verification required: send and validate the OTP before registering.
                    """
    )
    @PostMapping("/register")
    public BaseResponse<?> register(@RequestBody PasswordRequest passwordRequest) {
        return authenticationService.register(passwordRequest);
    }


    // Step 2: Save Additional Information
    @Operation(
            summary = "💾 Save User Additional Information",
            description = """
                    This endpoint allows users to save additional information such as 
                    their full name, phone number, and address. Ensure the user exists before calling this endpoint! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ User information saved successfully!
                    - **404**: ❌ User not found for the provided email.
                    """
    )
    @PostMapping("/save-user-info")
    public BaseResponse<?> saveUserInfo(
            @RequestBody UserInfoRequest userInfoRequest) {
        return authenticationService.saveUserInfo(userInfoRequest);
    }


    // Forget Password: (Reset the Password after OTP verification)
    @Operation(
            summary = "🔑 Reset Password after OTP Verification",
            description = """
                    This endpoint allows users to reset their password after successfully verifying their OTP. 
                    Make sure to provide the email associated with the account! 🎉
                    
                    **📩 Response Summary**:
                    - **200**: ✅ Password reset successfully!
                    - **404**: ❌ User not found for the provided email.
                    """
    )
    @PostMapping("/reset-password")
    public BaseResponse<?> resetPassword(@RequestBody PasswordRequest passwordRequest) {
        return authenticationService.resetPassword(passwordRequest);
    }
}

