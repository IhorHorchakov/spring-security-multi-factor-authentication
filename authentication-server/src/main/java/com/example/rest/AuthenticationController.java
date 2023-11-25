package com.example.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Controller
@Slf4j
public class AuthenticationController {

    @GetMapping("/login")
    public ModelAndView loginPage() {
        log.info("Redirecting a user to the 'Login' page");
        return new ModelAndView("login-page.html", OK);
    }

    @GetMapping("/home")
    public ModelAndView homePage(Principal principal) {
        String username = principal.getName();
        log.info("The user '{}' has been authorized", username);
        return new ModelAndView("home-page.html", Map.of("username", username), OK);
    }
}