package com.kshrd.kroya_api.config;

import com.kshrd.kroya_api.entity.UserEntity;
import com.kshrd.kroya_api.entity.token.TokenRepository;
import com.kshrd.kroya_api.repository.User.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository; // Assuming you have UserRepository to fetch UserEntity

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.info("Authorization header is null or does not start with Bearer String");
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        email = jwtService.extractUsername(response, jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Fetch the user entity directly without Optional
            UserEntity userEntity = userRepository.findByEmail(email);

            // Check if user exists and token is valid
            if (userEntity != null) {
                boolean isTokenValid = tokenRepository.findByToken(jwt)
                        .map(t -> !t.isTokenExpired() && !t.isTokenRevoked())
                        .orElse(false);

                if (jwtService.isTokenValid(jwt, userEntity) && isTokenValid) {
                    // Create authentication token using the UserEntity
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userEntity,
                            null,
                            userEntity.getAuthorities() // Use authorities from UserEntity
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    // Set the authentication token in the SecurityContext
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
