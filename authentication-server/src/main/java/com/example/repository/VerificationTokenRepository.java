package com.example.repository;

import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.VerificationTokenStatus;

import java.util.Optional;

public interface VerificationTokenRepository {

    void save(SmsCodeVerificationToken token);

    Optional<SmsCodeVerificationToken> getLatestPendingTokenByUserId(int userId);

    void updateStatusByTokenId(String tokenId, VerificationTokenStatus status);
}
