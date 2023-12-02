package com.example.repository.entity;

import lombok.Getter;

@Getter
public enum VerificationTokenStatus {
    VERIFICATION_PENDING,
    VERIFICATION_PASSED,
    VERIFICATION_FAILED
}
