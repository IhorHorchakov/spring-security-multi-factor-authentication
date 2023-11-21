package com.example.config;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.verify.VerifyClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NexmoConfig {
    @Value("nexmo.api.key")
    private String apiKey;
    @Value("nexmo.secret.key")
    private String secretKey;

//    @Bean
//    public NexmoClient nexmoClient() {
//        return NexmoClient.builder()
//                .applicationId("spring-security-multi-factor-authentication")
//                .apiKey(apiKey)
//                .apiSecret(secretKey)
//                .build();
//    }

//    @Bean
//    public VerifyClient nexmoVerifyClient(NexmoClient nexmoClient) {
//        return nexmoClient.getVerifyClient();
//    }
}
