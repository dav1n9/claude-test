package com.dav1n9.claudetest.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Claude Test API")
                        .description("Spring Boot CRUD REST API")
                        .version("v1.0.0")
                        .contact(new Contact()
                                .name("dav1n9")
                                .email("dav1n9@example.com")));
    }
}
