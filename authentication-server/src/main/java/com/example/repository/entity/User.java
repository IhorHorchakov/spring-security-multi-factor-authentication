package com.example.repository.entity;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class User {
    private Integer id;
    private String username;
    private String password;
    private String phoneNumber;
    private List<Authority> authorities;
}
