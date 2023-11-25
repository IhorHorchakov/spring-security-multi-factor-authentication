package com.example.service.verification;

public enum TokenVerificationStatus {
    TOKEN_IS_VALID,
    TOKEN_IS_MISSING,
    TOKEN_IS_NOT_CORRECT,
    TOKEN_IS_NOT_VALID;

    public boolean isValid() {
        return this == TOKEN_IS_VALID;
    }
    public boolean isMissing() {
        return this == TOKEN_IS_MISSING;
    }
    public boolean isNotCorrect() {
        return this == TOKEN_IS_NOT_CORRECT;
    }
    public boolean isNotValid() {
        return this == TOKEN_IS_NOT_VALID;
    }
}
