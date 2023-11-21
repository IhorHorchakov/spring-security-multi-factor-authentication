package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Controller
@Slf4j
public class ResourceController {

    @GetMapping("/login")
    public String loginPage() {
        log.info("Redirecting a user to the 'Login' page");
        return "login-page";
    }

    @PostMapping("/login")
    public void processLogin() {
        log.info("Processing login to verify user credentials");
    }

    @GetMapping("/verify-sms-code")
    public String verifySmsCodePage(Principal principal) {
        log.info("Redirecting a user to the 'Login' page");
        return "verify-sms-code-page";
    }

    @PostMapping("/verify-sms-code")
    public void processSmsCodeVerification(Principal principal) {
        log.info("Processing verifying user identity by SMS code");

    }

    @GetMapping("/home")
    public String home(Principal principal) {
        log.info(principal.getName() + " have been authorized");
        return "home-page";
    }


}
