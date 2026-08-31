package com.bootcamp.persona.application.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    private static final String API_TITLE = "Person Service API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "";

    @Bean
    public OpenAPI technologyOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION));
    }
}
