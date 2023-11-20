package com.example.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;


@Component
public class SmsCodeAuthenticationHandlerService implements AuthenticationSuccessHandler {

    @Autowired
    private VerificationTokenService verificationTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        verifySmsCodeFactor(request, response, authentication);
    }

    private void verifySmsCodeFactor(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }
}
