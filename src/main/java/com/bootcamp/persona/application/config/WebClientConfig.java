package com.bootcamp.persona.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient bootcampWebClient(
            WebClient.Builder builder,
            @Value("${bootcamp.service.url:http://localhost:8083}") String url) {
        return builder.baseUrl(url).build();
    }

    @Bean
    public WebClient reportWebClient(
            WebClient.Builder builder,
            @Value("${report.service.url:http://localhost:8085}") String url) {
        return builder.baseUrl(url).build();
    }
}
