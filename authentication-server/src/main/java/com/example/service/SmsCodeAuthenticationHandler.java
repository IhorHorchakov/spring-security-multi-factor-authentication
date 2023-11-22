package com.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
public class SmsCodeAuthenticationHandler implements AuthenticationSuccessHandler {
    private static final String VERIFICATION_URL = "/verify-sms-code";
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            String username = ((User) authentication.getPrincipal()).getUsername();
            log.info("User %s has passed form login. Let's verify the identity by SMS code".formatted(username));

            verificationTokenService.createSmsCodeVerificationToken(username);
            // TODO add the new role to allow user to be redirected to verification page
            new DefaultRedirectStrategy().sendRedirect(request, response, VERIFICATION_URL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
