package com.example.service.authentication;

import com.example.repository.entity.Authority;
import com.example.repository.entity.User;
import com.example.service.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class AuthenticationService {
    @Autowired
    private UserService userService;

    public User getPrincipalUser() {
        return userService.getByUserName(getPrincipalUsername());
    }

    public String getPrincipalUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public void grandPrincipalByAuthority(Authority authority) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //TODO check if the given authority is not added already
        List<GrantedAuthority> updatedAuthorities = new ArrayList<>(auth.getAuthorities());
        updatedAuthorities.add(authority);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public void logOutPrincipal() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
