package com.example.repository.entity;

import lombok.Data;

import java.util.Date;

@Data
public class VerificationToken {
    private String phone;
    private String requestId;
    private Date expirationDate;
}
