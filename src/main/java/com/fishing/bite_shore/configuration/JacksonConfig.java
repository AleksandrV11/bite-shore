package com.fishing.bite_shore.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JacksonConfig {
//    @Bean
//    public ObjectMapper getObjectMapper() {
//        return new ObjectMapper();
//    }

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.openweathermap.org")
                .build();
    }
}
