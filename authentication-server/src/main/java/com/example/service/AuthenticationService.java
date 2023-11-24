package com.example.service;

import com.example.repository.entity.Authority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationService {
    //TODO login programmatically

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
