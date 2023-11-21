package com.example.repository.entity;

import lombok.Getter;

@Getter
public enum VerificationTokenStatus {
    PENDING,
    VERIFICATION_PASSED,
    VERIFICATION_FAILED
}
