package com.example.repository;

import com.example.repository.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository {

    Optional<VerificationToken> getByPhoneNumber(String phoneNumber);
}
