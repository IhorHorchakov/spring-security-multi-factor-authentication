package com.example.client;

import com.nexmo.client.verify.VerifyResponse;
import org.springframework.stereotype.Component;

@Component
public class VerificationTokenClient {
//    @Autowired
//    private VerifyClient nexmoVerifyClient;
//    @Autowired
//    private NexmoClient nexmoClient;

    public VerifyResponse nexmoSendSmsCode(String phoneNumber, String applicationBrand) {
        String jsonResponse = "{\n" +
                "  \"request_id\": \"1234\",\n" +
                "  \"status\": \"OK\"\n" +
                "}";

        // if (verifyResponse.getErrorText() != null) {
        //            String errorMessage = "An error occurred while verifying user's phone number, cause: %s, status: %s".formatted(verifyResponse.getErrorText(), verifyResponse.getStatus());
        //            throw new RuntimeException(errorMessage);
        //        }
        return VerifyResponse.fromJson(jsonResponse);
    }

    public boolean nexmoCheckCode(String tokenId, String smsCode) {
        return Integer.parseInt(smsCode) == 1234;
    }
}
