package com.example.rest;

import com.example.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;

@Controller
@Slf4j
public class ResourceController {
    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping("/verify-sms-code")
    public String verifySmsCodePage(Principal principal) {
        log.info("Redirecting a user to the 'Verify sms code' page");
        return "verify-sms-code-page";
    }

    @PostMapping("/verify-sms-code")
    public void processSmsCodeVerification(@RequestParam("smsCode") String smsCode, Principal principal) {
        String username = principal.getName();
        log.info("Verifying user {} identity by the input SMS code {}", username, smsCode);
        verificationTokenService.verifySmsCode(username, smsCode);
    }

    @GetMapping("/home")
    public String home(Principal principal) {
        log.info(principal.getName() + " have been authorized");
        return "home-page";
    }


}
