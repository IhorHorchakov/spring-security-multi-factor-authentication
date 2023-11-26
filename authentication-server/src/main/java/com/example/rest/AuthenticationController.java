package com.example.rest;

import com.example.service.authentication.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Controller
@Slf4j
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/login")
    public ModelAndView loginPage() {
        log.info("Redirecting a user to the 'Login' page");
        return new ModelAndView("login-page.html", OK);
    }

    @GetMapping("/home")
    public ModelAndView homePage() {
        String username = authenticationService.getPrincipalUsername();
        log.info("The user '{}' has been authorized", username);
        return new ModelAndView("home-page.html", Map.of("username", username), OK);
    }
}
