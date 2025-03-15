package com.kshrd.kroya_api.config;

import com.kshrd.kroya_api.enums.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

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
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // ✅ Fix: Register CORS Configuration
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/v1/auth/**",
                                "/api/v1/fileView/**",
                                "/api/v1/guest-user/**",
                                "/api/v1/favorite/**",
                                "/api/v1/address/**",
                                "/api/v1/user/**",
                                "/api/v1/guest-user/feedback/{foodId}",
                                "/v2/api-docs",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-resources",
                                "/swagger-resources/**",
                                "/configuration/ui",
                                "/configuration/security",
                                "/swagger-ui/**",
                                "/webjars/**",
                                "/swagger-ui.html"
                        ).permitAll()

                        // Cuisine & Category: Allow authenticated users to CREATE (POST) & GET
                        .requestMatchers(HttpMethod.GET, "/api/v1/cuisine/**", "/api/v1/category/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/v1/cuisine/**", "/api/v1/category/**").authenticated()

                        // ADMIN-only routes
                        .requestMatchers(
                                "/api/v1/food-sell/**",
                                "/api/v1/food-recipe/post-food-recipe",
                                "/api/v1/food-recipe/edit-food-recipe/**",
                                "/api/v1/food-recipe/delete/**",
                                "/api/v1/user/all",
                                "/api/v1/foods/**",
                                "/api/v1/category/**"
                        ).hasRole("ADMIN")

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

    /**
     * ✅ Fix: Defines CORS Configuration to allow frontend & Swagger UI
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(List.of(
                "https://krorya-dashbaord-git-newdeploy-dear0001s-projects.vercel.app", // ✅ Frontend (Vercel)
                "https://kroryaapi-api.up.railway.app", // ✅ Backend (For Swagger UI)
                "http://localhost:3000", // ✅ Local frontend testing
                "http://localhost:8080" // ✅ Local backend testing
        ));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private void accessDeniedHandler(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) {
        jwtService.jwtExceptionHandler(response, ResponseMessage.FORBIDDEN);
    }

    public void unauthorizedHandler(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        jwtService.jwtExceptionHandler(response, ResponseMessage.UNAUTHORIZED);
    }
}
