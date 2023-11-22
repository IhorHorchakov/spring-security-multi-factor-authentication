package com.example.client;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.verify.VerifyClient;
import com.nexmo.client.verify.VerifyResponse;
import com.nexmo.client.verify.VerifyStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenClient {
//    @Autowired
//    private VerifyClient nexmoVerifyClient;
//    @Autowired
//    private NexmoClient nexmoClient;

    public VerifyResponse nexmoVerifyPhoneNumber(String phoneNumber, String applicationBrand) {
        String jsonResponse = "{\n" +
                "  \"request_id\": \"123456\",\n" +
                "  \"status\": \"OK\"\n" +
                "}";
        return VerifyResponse.fromJson(jsonResponse);
    }
}
