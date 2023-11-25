package com.example.repository;

import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.VerificationTokenStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.repository.entity.VerificationTokenStatus.PENDING;

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
    public Optional<SmsCodeVerificationToken> getPendingActualLatestTokenByUserId(int userId) {
        return this.storage.values().stream()
                // check the token is 'pending'
                .filter(token -> token.getUserId() == userId && token.getStatus() == PENDING)
                // check the token is not expired - 'actual'
                .filter(token -> token.getExpirationDate().isAfter(LocalDateTime.now()))
                // get the 'latest' token sorted by created date
                .min(Comparator.comparing(SmsCodeVerificationToken::getCreatedDate));
    }

    @Override
    public void updateStatusByTokenId(String tokenId, VerificationTokenStatus status) {
        SmsCodeVerificationToken token = this.storage.get(tokenId);
        if (token != null) {
            token.setStatus(status);
        }
    }

    @Override
    public void updateAttemptByTokenId(String tokenId, int attempt) {
        SmsCodeVerificationToken token = this.storage.get(tokenId);
        if (token != null) {
            token.setAttempt(attempt);
        }
    }

    @Override
    public void deleteDeprecatedTokens() {
        this.storage.values().stream()
                .filter(token -> token.getExpirationDate().isBefore(LocalDateTime.now()))
                .forEach(deprecatedToken -> this.storage.remove(deprecatedToken.getTokenId()));
    }

}
