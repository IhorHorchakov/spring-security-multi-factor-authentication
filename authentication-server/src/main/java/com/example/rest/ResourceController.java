package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@Slf4j
public class ResourceController {

    @GetMapping("/login")
    public String login() {
        log.info("1. Redirecting a user to the 'Login' page");
        return "login-page";
    }

    @GetMapping("/verify-sms-code")
    public String verifySmsCode(Principal principal) {
        return "verify-sms-code-page";
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        log.info(principal.getName() + " have been authorized");
        return "home-page";
    }


}
