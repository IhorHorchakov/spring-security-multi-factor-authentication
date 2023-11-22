package com.example.repository;

import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.VerificationTokenStatus;
import org.springframework.stereotype.Repository;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class InMemoryVerificationTokenRepository implements VerificationTokenRepository {
    private final Map<String, SmsCodeVerificationToken> storage = new LinkedHashMap<>();

    @Override
    public void save(SmsCodeVerificationToken token) {
        if (token.getTokenId() == null) {
            throw new IllegalArgumentException("Failed to save token, the tokenId is null");
        }
        this.storage.put(token.getTokenId(), token);
    }

    @Override
    public Optional<SmsCodeVerificationToken> getLatestPendingTokenByUserId(int userId) {

        return Optional.empty();
    }

    @Override
    public void updateStatusByTokenId(String tokenId, VerificationTokenStatus status) {

    }

}
