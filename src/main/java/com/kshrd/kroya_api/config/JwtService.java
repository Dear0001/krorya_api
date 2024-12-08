package com.kshrd.kroya_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kshrd.kroya_api.entity.UserEntity;
import com.kshrd.kroya_api.enums.IResponseMessage;
import com.kshrd.kroya_api.enums.ResponseMessage;
import com.kshrd.kroya_api.payload.BaseResponse;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@EnableWebSecurity
public class JwtService {

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Value("${security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${security.jwt.refresh-token.expiration}")
    private long refreshExpiration;

    @Autowired
    private final ObjectMapper mapper;

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    public JwtService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    // Extract the username (email) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // Extract a specific claim from the token
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate JWT token using UserEntity directly
    public String generateToken(UserEntity user) {
        return generateToken(new HashMap<>(), user);
    }

    // Generate JWT token with additional claims
    public String generateToken(Map<String, Object> extraClaims, UserEntity user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    // Generate refresh token using UserEntity
    public String generateRefreshToken(UserEntity user) {
        return buildToken(new HashMap<>(), user, refreshExpiration);
    }

    // Helper method to build the token
    private String buildToken(Map<String, Object> extraClaims, UserEntity user, long expiration) {
        // Add additional claims
        extraClaims.put("userId", user.getId());               // Include userId
        extraClaims.put("gmail", user.getEmail());             // Include email as gmail
        extraClaims.put("roles", user.getRole());              // Include role as string

        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername()) // Set subject as username (email)
                .setIssuedAt(new Date(System.currentTimeMillis()))  // Set issued at time
                .setExpiration(new Date(System.currentTimeMillis() + expiration))  // Set expiration
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)  // Sign with key
                .compact();
    }

    // Validate if token is valid for a given user
    public boolean isTokenValid(String token, UserEntity user) {
        final String username = extractUsername(token);
        return (username.equals(user.getUsername())) && !isTokenExpired(token);
    }

    // Check if the token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract expiration date from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract all claims from the token
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get the signing key from the secret
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Extract username and handle JWT exceptions
    public String extractUsername(HttpServletResponse response, String jwt) {
        try {
            return extractUsername(jwt);
        } catch (SignatureException e) {
            jwtExceptionHandler(response, ResponseMessage.INVALID_TOKEN_SIGNATURE);
        } catch (MalformedJwtException | IllegalArgumentException e) {
            jwtExceptionHandler(response, ResponseMessage.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            jwtExceptionHandler(response, ResponseMessage.TOKEN_EXPIRED);
        } catch (UnsupportedJwtException e) {
            jwtExceptionHandler(response, ResponseMessage.UNSUPPORTED_TOKEN);
        }
        return null;
    }

    // Handle JWT exceptions and return custom responses
    public void jwtExceptionHandler(HttpServletResponse response, IResponseMessage msg) {
        try (ServletServerHttpResponse res = new ServletServerHttpResponse(response)) {
            res.getServletResponse().setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
            logger.info("JWT Exception Handler : {}", msg.getMessage());
            res.getBody().write(mapper.writeValueAsString(BaseResponse.builder()
                    .responseMessage(msg).build()).getBytes());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}