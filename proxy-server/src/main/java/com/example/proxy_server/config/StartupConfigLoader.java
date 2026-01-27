package com.example.proxy_server.config;

import com.example.proxy_server.util.OriginHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupConfigLoader implements CommandLineRunner {

    private final Logger logger = LoggerFactory.getLogger(StartupConfigLoader.class);

    private final ProxyConfig proxyConfig;
    private final OriginHolder originHolder;

    public StartupConfigLoader(ProxyConfig proxyConfig, OriginHolder originHolder) {
        this.proxyConfig = proxyConfig;
        this.originHolder = originHolder;
    }

    @Override
    public void run(String... args) {

        if (proxyConfig.getOrigin() == null) {
            logger.info("Missing required origin {}", (Object) null);
            System.exit(1);
        }

        originHolder.setOrigin(proxyConfig.getOrigin());

        logger.info("Origin set to: {}", originHolder.getOrigin());
    }
}

