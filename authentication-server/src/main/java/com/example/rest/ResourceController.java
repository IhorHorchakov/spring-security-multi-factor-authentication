package com.example.rest;

import com.example.service.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
@Slf4j
public class ResourceController {
    @Autowired
    private VerificationTokenService verificationTokenService;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        log.info("Redirecting a user to the 'Login' page");
        return new ModelAndView("login-page.html", HttpStatus.OK);
    }

    @GetMapping("/verify-sms-code")
    public ModelAndView verifySmsCodePage(Principal principal) {
        log.info("Redirecting a user to the 'Verify sms code' page");
        return new ModelAndView("verify-sms-code-page.html", HttpStatus.OK);
    }

    @PostMapping("/verify-sms-code")
    public RedirectView processSmsCodeVerification(@RequestParam("smsCode") String smsCode, Principal principal) {
        String username = principal.getName();
        log.info("Verifying user {} identity by the input SMS code {}", username, smsCode);
        boolean isValidSmsCode = verificationTokenService.verifySmsCode(username, smsCode);
        if (isValidSmsCode) {
            log.info("The given SMS code '{}' is VALID for user '{}'", smsCode, username);
            return new RedirectView("/home");
        } else {
            log.info("The given SMS code '{}' is NOT VALID for user '{}'", smsCode, username);
            return new RedirectView("/login");
        }
    }

    @GetMapping("/home")
    public ModelAndView home(Principal principal) {
        log.info(principal.getName() + " have been authorized");
        return new ModelAndView("home-page.html", HttpStatus.OK);
    }


}
