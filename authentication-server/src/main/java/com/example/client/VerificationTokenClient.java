package com.example.client;

import com.vonage.client.VonageClient;
import com.vonage.client.verify.CheckResponse;
import com.vonage.client.verify.VerifyResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.example.ApplicationConstants.APPLICATION_BRAND;

@Component
public class VerificationTokenClient {
    @Autowired
    private VonageClient vonageClient;

    public VerifyResponse nexmoSendSmsCode(String phoneNumber) {
        return vonageClient.getVerifyClient().verify(phoneNumber, APPLICATION_BRAND);
    }

    public CheckResponse nexmoCheckCode(String requestId, String smsCode) {
        return vonageClient.getVerifyClient().check(requestId, smsCode);
    }
}
