package com.kshrd.kroya_api.config;

import com.kshrd.kroya_api.enums.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no auth required)
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/provider/**",
                                "/api/v1/fileView/**",
                                "/api/v1/category/**",
                                "/api/v1/address/**",
                                "/api/v1/guest-user/**", // This covers all guest-user endpoints
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html",

                                // Read-only admin endpoints
                                "/api/v1/user/all", // Get all users
                                "/api/v1/dashboard/counts", // Dashboard stats
                                "/api/v1/cuisine/all" // Get all cuisines
                        ).permitAll()

                        // ADMIN-only write operations
                        .requestMatchers(
                                "/api/v1/food-recipe/post-food-recipe",
                                "/api/v1/food-recipe/edit-food-recipe/**",
                                "/api/v1/food-recipe/delete/**",
                                "/api/v1/user/deleteUserById/**"
                        ).hasRole("ADMIN")

                        // Protected endpoints (require auth)
                        .requestMatchers(
                                "/api/v1/favorite/**", // Favorite operations
                                "/api/v1/feedback/**", // Feedback operations
                                "/api/v1/user/profile", // User profile
                                "/api/v1/user/edit-profile", // Profile update
                                "/api/v1/user/device-token/**" // Device tokens
                        ).authenticated()

                        .anyRequest().authenticated()
                )

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                .accessDeniedHandler(this::accessDeniedHandler)
                                .authenticationEntryPoint(this::unauthorizedHandler)
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(log -> log
                        .logoutUrl("/api/v1/auth/logout")
                        .addLogoutHandler(logoutHandler)
                        .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }

    private void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        jwtService.jwtExceptionHandler(response, ResponseMessage.FORBIDDEN);
    }

    public void unauthorizedHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        jwtService.jwtExceptionHandler(response, ResponseMessage.UNAUTHORIZED);
    }
}