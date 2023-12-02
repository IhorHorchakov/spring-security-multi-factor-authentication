package com.example.rest;

import com.example.repository.entity.Authority;
import com.example.repository.entity.User;
import com.example.service.authentication.AuthenticationService;
import com.example.service.verification.TokenVerificationStatus;
import com.example.service.verification.VerificationTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

@Controller
@Slf4j
public class VerificationController {
    @Autowired
    private VerificationTokenService verificationTokenService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/verify-sms-code")
    @PreAuthorize("hasAuthority('READY_FOR_SMS_CODE_VERIFICATION')")
    public ModelAndView verifySmsCodePage() {
        log.info("Redirecting a user to the 'Verify sms code' page");
        return new ModelAndView("verify-sms-code-page.html", OK);
    }

    @PostMapping("/verify-sms-code")
    @PreAuthorize("hasAuthority('READY_FOR_SMS_CODE_VERIFICATION')")
    public Object processSmsCodeVerification(@RequestParam("smsCode") String smsCode, RedirectAttributes redirectAttributes) {
        String username = authenticationService.getPrincipalUsername();
        log.info("Verifying user {} identity by the input SMS code {}", username, smsCode);
        TokenVerificationStatus status = verificationTokenService.verifySmsCode(username, smsCode);
        if (status.isValid()) {
            return handleValidStatus(smsCode, username, redirectAttributes);
        } else if (status.isNotCorrect()){
            return handleNotCorrectStatus(smsCode, username);
        } else if (status.isMissing()) {
            return handleMissingStatus(username);
        } else if (status.isNotValid()) {
            return handleNotValidStatus(smsCode, username);
        } else {
            authenticationService.logOutPrincipal();
            throw new IllegalStateException("Failed to handle the given token");
        }
    }

    @PostMapping("/generate-sms-code")
    public ModelAndView generateSmsCode() {
        User principal = authenticationService.getPrincipalUser();
        verificationTokenService.generateVerificationToken(principal);
        return new ModelAndView("verify-sms-code-page.html", OK);
    }

    private RedirectView handleValidStatus(String smsCode, String username, RedirectAttributes redirectAttributes) {
        log.info("The given SMS code '{}' is VALID for user '{}'", smsCode, username);
        authenticationService.grandPrincipalByAuthority(Authority.MFA_AUTHENTICATED);
        redirectAttributes.addFlashAttribute("flashAttribute", "redirectWithRedirectAttributes");
        return new RedirectView("/home");
    }

    private ModelAndView handleNotCorrectStatus(String smsCode, String username) {
        log.info("The given SMS code '{}' is NOT CORRECT for user '{}', try one more time", smsCode, username);
        Map<String, String> mavParams = Map.of("errorCause", "The code '%s' is not correct, try one more time".formatted(smsCode));
        return new ModelAndView("verify-sms-code-page.html", mavParams, ACCEPTED);
    }

    private ModelAndView handleMissingStatus(String username) {
        log.info("No pending token found, user '{}' has to pass login procedure again", username);
        authenticationService.logOutPrincipal();
        return new ModelAndView("login.html");
    }

    private ModelAndView handleNotValidStatus(String smsCode, String username) {
        log.info("The given SMS code '{}' is NOT VALID for user '{}'", smsCode, username);
        authenticationService.logOutPrincipal();
        return new ModelAndView("login.html");
    }
}
