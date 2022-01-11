package com.example.oauth2.client.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApi3Config {
    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        final String apiTitle = "oauth2 client example";
        OpenAPI openAPI = new OpenAPI();
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.addList(securitySchemeName);
        openAPI.addSecurityItem(securityRequirement);

        Components components = new Components();
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.name(securitySchemeName).type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT");
        components.addSecuritySchemes(securitySchemeName, securityScheme);
        openAPI.components(components).info(new Info().title(apiTitle).version("0.0.1"));
        return openAPI;
    }
}