package com.example.service.verification;

import com.example.client.VerificationTokenClient;
import com.example.repository.VerificationTokenRepository;
import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.User;
import com.example.repository.entity.VerificationTokenStatus;
import com.example.service.user.UserService;
import com.vonage.client.verify.CheckResponse;
import com.vonage.client.verify.VerifyResponse;
import com.vonage.client.verify.VerifyStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.repository.entity.VerificationTokenStatus.VERIFICATION_FAILED;
import static com.example.repository.entity.VerificationTokenStatus.VERIFICATION_PASSED;
import static com.example.service.verification.TokenVerificationStatus.TOKEN_IS_MISSING;
import static com.example.service.verification.TokenVerificationStatus.TOKEN_IS_NOT_CORRECT;
import static com.example.service.verification.TokenVerificationStatus.TOKEN_IS_NOT_VALID;
import static com.example.service.verification.TokenVerificationStatus.TOKEN_IS_VALID;
import static com.example.ApplicationConstants.SMS_CODE_TOKEN_MAX_VERIFICATION_ATTEMPTS_ALLOWED;
import static com.example.ApplicationConstants.SMS_CODE_TOKEN_TIME_LIFE_SECONDS;

@Service
@Slf4j
public class VerificationTokenService {
    @Autowired
    private VerificationTokenRepository tokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenClient tokenClient;

    public void publishSmsCodeVerificationToken(String username) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = tokenRepository.getPendingActualLatestTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            generateVerificationToken(user);
        }
    }

    public void generateVerificationToken(User user) {
        VerifyResponse verifyResponse = tokenClient.nexmoSendSmsCode(user.getPhoneNumber());
        if (VerifyStatus.OK == verifyResponse.getStatus()) {
            SmsCodeVerificationToken token = createSmsCodeVerificationToken(verifyResponse.getRequestId(), user);
            tokenRepository.save(token);
        } else {
            log.error("Failed to generate new sms code, status: {}, cause: {}", verifyResponse.getStatus(), verifyResponse.getErrorText());
            throw new RuntimeException("Failed to generate new sms code");
        }
    }

    public SmsCodeVerificationToken createSmsCodeVerificationToken(String requestId, User user) {
        SmsCodeVerificationToken token = new SmsCodeVerificationToken();
        token.setTokenId(requestId);
        token.setUserId(user.getId());
        token.setPhoneNumber(user.getPhoneNumber());
        token.setStatus(VerificationTokenStatus.PENDING);
        token.setAttempt(SMS_CODE_TOKEN_MAX_VERIFICATION_ATTEMPTS_ALLOWED);
        LocalDateTime createdDate = LocalDateTime.now();
        token.setCreatedDate(createdDate);
        token.setExpirationDate(createdDate.plusSeconds(SMS_CODE_TOKEN_TIME_LIFE_SECONDS));
        return token;
    }

    public TokenVerificationStatus verifySmsCode(String username, String smsCode) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = tokenRepository.getPendingActualLatestTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            return TOKEN_IS_MISSING;
        } else {
            SmsCodeVerificationToken token = optionalToken.get();
            String tokenId = token.getTokenId();
            CheckResponse checkResponse = tokenClient.nexmoCheckCode(tokenId, smsCode);
            if (VerifyStatus.OK == checkResponse.getStatus()) {
                tokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_PASSED);
                return TOKEN_IS_VALID;
            } else {
                int numberOfAllowedVerificationAttempts = token.getAttempt() - 1;
                if (numberOfAllowedVerificationAttempts >= 0) {
                    tokenRepository.updateAttemptByTokenId(tokenId, numberOfAllowedVerificationAttempts);
                    return TOKEN_IS_NOT_CORRECT;
                } else {
                    tokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_FAILED);
                    return TOKEN_IS_NOT_VALID;
                }
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteDeprecatedTokens() {
        tokenRepository.deleteDeprecatedTokens();
    }
}
