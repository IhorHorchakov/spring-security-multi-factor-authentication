package com.example.repository.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class SmsCodeVerificationToken {
    private String tokenId;
    private int userId;
    private String phoneNumber;
    private LocalDateTime expirationDate;
    private VerificationTokenStatus status;
}
