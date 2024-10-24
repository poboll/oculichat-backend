package com.caiths.caiapiinterface;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component  // 确保这个注解存在
public class CaiApiConfig {

    @Value("${caiapi.client.access-key}")
    private String accessKey;

    @Value("${caiapi.client.secret-key}")
    private String secretKey;

    public String getAccessKey() {
        return accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }
}
