package com.kshrd.kroya_api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Krorya API - OpenAPI Specification",
                version = "1.0",
                description = "OpenAPI documentation for Krorya API",
                termsOfService = "http://localhost:8080/terms",
                contact = @Contact(
                        name = "Krorya API",
                        url = "https://seavphovmhob-api.onrender.com",
                        email = "dochkouern@gmail.com"
                ),
                license = @License(
                        name = "MIT License",
                        url = "https://opensource.org/licenses/MIT"
                )
        ),
        servers = {
                @Server(
                        description = "🚀 Production Server",
                        url = "https://seavphovmhob-api.onrender.com"
                ),
                @Server(
                        description = "🖥️ Local Development",
                        url = "http://localhost:8080"
                )
        },
        security = @SecurityRequirement(name = "bearerAuth") // Applies to all endpoints globally
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth with Bearer Token",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}