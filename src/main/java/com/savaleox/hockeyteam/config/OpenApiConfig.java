package com.savaleox.hockeyteam.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Hockey API",
                version = "v1",
                description = "API for teams, players, their statistics and achievements",
                contact = @Contact(name = "SavaleOx",
                url = "https://github.com/SavaleOx"),
                license = @License(name = "Internal")
        )
)
public class OpenApiConfig {
}
