package com.example.service;

import com.example.client.VerificationTokenClient;
import com.example.repository.VerificationTokenRepository;
import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.User;
import com.example.repository.entity.VerificationTokenStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.repository.entity.VerificationTokenStatus.VERIFICATION_FAILED;
import static com.example.repository.entity.VerificationTokenStatus.VERIFICATION_PASSED;
import static com.example.service.VerificationTokenService.TokenVerificationStatus.TOKEN_IS_MISSING;
import static com.example.service.VerificationTokenService.TokenVerificationStatus.TOKEN_IS_NOT_VALID;
import static com.example.service.VerificationTokenService.TokenVerificationStatus.TOKEN_IS_VALID;
import static com.example.service.VerificationTokenService.TokenVerificationStatus.TOKEN_IS_NOT_CORRECT;
import static com.example.util.ApplicationConstants.APPLICATION_BRAND;
import static com.example.util.ApplicationConstants.SMS_CODE_TOKEN_TIME_LIFE_SECONDS;

@Service
@Slf4j
public class VerificationTokenService {
    private static final Integer MAX_VERIFICATION_ATTEMPTS = 2;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private VerificationTokenClient verificationTokenClient;

    public void publishSmsCodeVerificationToken(String username) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getPendingActualLatestTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            verificationTokenClient.nexmoSendSmsCode(user.getPhoneNumber(), APPLICATION_BRAND);
            SmsCodeVerificationToken token = createToken(user);
            verificationTokenRepository.save(token);
        }
    }

    private SmsCodeVerificationToken createToken(User user) {
        SmsCodeVerificationToken token = new SmsCodeVerificationToken();
        token.setTokenId(UUID.randomUUID().toString());
        token.setUserId(user.getId());
        token.setPhoneNumber(user.getPhoneNumber());
        token.setStatus(VerificationTokenStatus.PENDING);
        token.setAttempt(MAX_VERIFICATION_ATTEMPTS);
        LocalDateTime createdDate = LocalDateTime.now();
        token.setCreatedDate(createdDate);
        token.setExpirationDate(createdDate.plusSeconds(SMS_CODE_TOKEN_TIME_LIFE_SECONDS));
        return token;
    }

    //TODO handle the case when user have several attempts to enter the sms code
    public TokenVerificationStatus verifySmsCode(String username, String smsCode) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getPendingActualLatestTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            return TOKEN_IS_MISSING;
        } else {
            SmsCodeVerificationToken token = optionalToken.get();
            String tokenId = token.getTokenId();
            boolean isSmsCodeValid = verificationTokenClient.nexmoCheckCode(tokenId, smsCode);
            if (isSmsCodeValid) {
                verificationTokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_PASSED);
                return TOKEN_IS_VALID;
            } else {
                int numberOfAllowedVerificationAttempts = token.getAttempt() - 1;
                if (numberOfAllowedVerificationAttempts >= 0) {
                    verificationTokenRepository.updateAttemptByTokenId(tokenId, numberOfAllowedVerificationAttempts);
                    return TOKEN_IS_NOT_CORRECT;
                } else {
                    verificationTokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_FAILED);
                    return TOKEN_IS_NOT_VALID;
                }
            }
        }
    }

    public enum TokenVerificationStatus {
        TOKEN_IS_VALID,
        TOKEN_IS_MISSING,
        TOKEN_IS_NOT_CORRECT,
        TOKEN_IS_NOT_VALID;

        public boolean isValid() {
            return this == TOKEN_IS_VALID;
        }
        public boolean isMissing() {
            return this == TOKEN_IS_MISSING;
        }
        public boolean isNotCorrect() {
            return this == TOKEN_IS_NOT_CORRECT;
        }
        public boolean isNotValid() {
            return this == TOKEN_IS_NOT_VALID;
        }
    }
}
