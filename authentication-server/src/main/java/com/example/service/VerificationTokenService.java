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

    public void publishSmsCodeVerificationToken(String username) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getLatestPendingTokenByUserId(user.getId());
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
        LocalDateTime createdDate = LocalDateTime.now();
        token.setCreatedDate(createdDate);
        token.setExpirationDate(createdDate.plusSeconds(SMS_CODE_TOKEN_TIME_LIFE_SECONDS));
        return token;
    }

    //TODO handle the case when user have several attempts to enter the sms code
    public boolean verifySmsCode(String username, String smsCode) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getLatestPendingTokenByUserId(user.getId());
        if (optionalToken.isEmpty()) {
            return false;
        } else {
            SmsCodeVerificationToken token = optionalToken.get();
            String tokenId = token.getTokenId();
            boolean isSmsCodeValid = verificationTokenClient.nexmoCheckCode(tokenId, smsCode);
            if (isSmsCodeValid) {
                verificationTokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_PASSED);
            } else {
                verificationTokenRepository.updateStatusByTokenId(tokenId, VERIFICATION_FAILED);
            }
            return isSmsCodeValid;
        }
    }
}
