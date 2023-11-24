package com.example.repository.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SmsCodeVerificationToken {
    private String tokenId;
    private int userId;
    private String phoneNumber;
    private LocalDateTime createdDate;
    private LocalDateTime expirationDate;
    private int attempt;
    private VerificationTokenStatus status;
}
