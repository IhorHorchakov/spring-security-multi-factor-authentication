package com.example.repository;

import com.example.repository.entity.VerificationToken;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class InMemoryVerificationTokenRepository implements VerificationTokenRepository {
    @Override
    public Optional<VerificationToken> getByPhoneNumber(String phoneNumber) {
        return Optional.empty();
    }
}
