package com.example.proxy_server.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfiguration {

    @Value("${spring.profiles.active")
    public String activeProfile;

    @Bean
    public RestClient restClient(){
        return RestClient
                .builder()
                .baseUrl(getUrl())
                .build();
    }

    private String getUrl() {
        switch (activeProfile.toUpperCase()) {
            case "DEVELOPMENT":
            case "STAGING":
            case "PRODUCTION":
            default:
                return "http://localhost:8081";
        }
    }


}
