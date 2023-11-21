package com.example.service;

import com.example.repository.entity.SmsCodeVerificationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.Principal;


@Component
public class SmsCodeAuthenticationHandler implements AuthenticationSuccessHandler {
    private static final String VERIFICATION_URL = "/verify-sms-code";
    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            Principal principal = (Principal) authentication.getPrincipal();
            SmsCodeVerificationToken token = verificationTokenService.getSmsCodeVerificationToken(principal.getName());

            new DefaultRedirectStrategy().sendRedirect(request, response, VERIFICATION_URL);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
