package com.example.service;

import com.example.repository.VerificationTokenRepository;
import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.User;
import com.example.repository.entity.VerificationTokenStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenService {
    private static final Integer SMS_CODE_TOKEN_TIME_LIFE_SECONDS = 30;
    @Autowired
    private VerificationTokenRepository verificationTokenRepository;
    @Autowired
    private UserService userService;

    public SmsCodeVerificationToken getSmsCodeVerificationToken(String username) {
        User user = userService.getByUserName(username);
        Optional<SmsCodeVerificationToken> optionalToken = verificationTokenRepository.getLatestPendingTokenByUserId(user.getId());
        if (optionalToken.isPresent()) {
            return optionalToken.get();
        } else {
            SmsCodeVerificationToken token = createToken(user);
            verificationTokenRepository.save(token);
            return token;
        }
    }

    private SmsCodeVerificationToken createToken(User user) {
        SmsCodeVerificationToken token = new SmsCodeVerificationToken();
        token.setTokenId(UUID.randomUUID().toString());
        token.setUserId(user.getId());
        token.setPhoneNumber(user.getPhoneNumber());
        token.setStatus(VerificationTokenStatus.PENDING);
        token.setExpirationDate(LocalDateTime.now().plusSeconds(SMS_CODE_TOKEN_TIME_LIFE_SECONDS));
        return token;
    }

}
