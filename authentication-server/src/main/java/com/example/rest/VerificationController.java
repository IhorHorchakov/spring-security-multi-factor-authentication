package com.example.rest;

import com.example.repository.entity.Authority;
import com.example.service.AuthenticationService;
import com.example.service.VerificationTokenService;
import com.example.service.VerificationTokenService.TokenVerificationStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Controller
@Slf4j
public class VerificationController {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/verify-sms-code")
    public ModelAndView verifySmsCodePage(Principal principal) {
        log.info("Redirecting a user to the 'Verify sms code' page");
        return new ModelAndView("verify-sms-code-page.html", OK);
    }

    @PostMapping("/verify-sms-code")
    public ModelAndView processSmsCodeVerification(@RequestParam("smsCode") String smsCode, Authentication authentication) {
        String username = authentication.getName();
        log.info("Verifying user {} identity by the input SMS code {}", username, smsCode);
        TokenVerificationStatus status = verificationTokenService.verifySmsCode(username, smsCode);
        if (status.isValid()) {
            log.info("The given SMS code '{}' is VALID for user '{}'", smsCode, username);
            authenticationService.grandPrincipalByAuthority(Authority.MFA_AUTHENTICATED);
            return new ModelAndView("redirect:/home");
        } else if (status.isNotCorrect()){
            log.info("The given SMS code '{}' is NOT CORRECT for user '{}', try one more time", smsCode, username);
            Map<String, String> params = Map.of("errorCause", "The code '%s' is not correct, try one more time".formatted(smsCode));
            return new ModelAndView("/verify-sms-code-page.html", params);
        } else if (status.isMissing()) {
            log.info("No pending token found, user '{}' has to pass login procedure again", username);
        } else if (status.isNotValid()) {
            log.info("The given SMS code '{}' is NOT VALID for user '{}'", smsCode, username);
        }
        authenticationService.logOutPrincipal();
        return new ModelAndView("/login");
    }


}
