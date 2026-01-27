package com.example.proxy_server.config;

import lombok.Data;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "proxy")
@Data
public class ProxyConfig {

    private int port;
    private String origin;

}
