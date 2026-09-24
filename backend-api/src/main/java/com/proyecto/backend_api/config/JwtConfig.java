package com.proyecto.backend_api.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String secretKey;
    private long expiration;
    private String prefix;

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecret(String secretKey) {
        this.secretKey = secretKey;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix){
        this.prefix = prefix;   
    }

}
