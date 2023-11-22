package com.example.service;

import com.example.client.VerificationTokenClient;
import com.example.repository.VerificationTokenRepository;
import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.User;
import com.example.repository.entity.VerificationTokenStatus;
import com.nexmo.client.verify.VerifyResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.util.ApplicationConstants.APPLICATION_BRAND;
import static com.example.util.ApplicationConstants.SMS_CODE_TOKEN_TIME_LIFE_SECONDS;

@Service
@Slf4j
public class VerificationTokenService {
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenClient verificationTokenClient;

    public void createSmsCodeVerificationToken(String username) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getLatestPendingTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            SmsCodeVerificationToken token = createToken(user);
            verificationTokenRepository.save(token);
        }
    }

    private SmsCodeVerificationToken createToken(User user) {
        VerifyResponse verifyResponse = verificationTokenClient.nexmoVerifyPhoneNumber(user.getPhoneNumber(), APPLICATION_BRAND);
        if (verifyResponse.getErrorText() != null) {
            String errorMessage = "An error occurred while verifying user's phone number, cause: %s, status: %s".formatted(verifyResponse.getErrorText(), verifyResponse.getStatus());
            throw new RuntimeException(errorMessage);
        } else {
            SmsCodeVerificationToken token = new SmsCodeVerificationToken();
            token.setTokenId(UUID.randomUUID().toString());
            token.setUserId(user.getId());
            token.setPhoneNumber(user.getPhoneNumber());
            token.setStatus(VerificationTokenStatus.PENDING);
            token.setExpirationDate(LocalDateTime.now().plusSeconds(SMS_CODE_TOKEN_TIME_LIFE_SECONDS));
            return token;
        }
    }

    public void verifySmsCode(String username, String smsCode) {

    }
}
