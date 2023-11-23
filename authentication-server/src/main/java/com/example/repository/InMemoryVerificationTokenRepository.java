package com.example.repository;

import com.example.repository.entity.SmsCodeVerificationToken;
import com.example.repository.entity.VerificationTokenStatus;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public Optional<SmsCodeVerificationToken> getLatestPendingTokenByUserId(int userId) {
        return this.storage.values().stream()
                .filter(token -> token.getUserId() == userId && token.getStatus() == PENDING)
                .min(Comparator.comparing(SmsCodeVerificationToken::getCreatedDate));
    }

    @Override
    public void updateStatusByTokenId(String tokenId, VerificationTokenStatus status) {
        SmsCodeVerificationToken token = this.storage.get(tokenId);
        if (token != null) {
            token.setStatus(status);
        }
    }

}
