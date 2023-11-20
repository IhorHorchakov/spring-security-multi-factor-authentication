package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@EnableMethodSecurity
@RestController
@Slf4j
public class LoginController {


    @PostMapping("/login")
    public String login(Principal principal) {
        return "login-page";
    }
}