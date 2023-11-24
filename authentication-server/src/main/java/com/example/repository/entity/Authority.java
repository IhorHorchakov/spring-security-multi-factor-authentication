package com.example.repository.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Authority implements GrantedAuthority {
    READY_FOR_SMS_CODE_VERIFICATION,
    MFA_AUTHENTICATED;

    @Override
    public String getAuthority() {
        return name();
    }
}
