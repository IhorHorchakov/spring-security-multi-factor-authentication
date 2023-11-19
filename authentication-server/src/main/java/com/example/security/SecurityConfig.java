package com.example.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class SecurityConfig {

    @Autowired
    private SmsCodeAuthenticationHandler bySmsCodeAuthenticationHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User
                .withUsername("johndoe@gmail.com")
                .password(passwordEncoder().encode("johndoe"))
                .authorities(List.of())
                .build());
        manager.createUser(User
                .withUsername("fairyprincess@gmail.com")
                .password(passwordEncoder().encode("fairyprincess"))
                .authorities(List.of())
                .build());
        return manager;
    }

    /**
     * The implementation of AuthenticationManager that uses UserCredentials (login, password) to authenticate a user
     * by leveraging DaoAuthenticationProvider.
     */
    @Bean
    public AuthenticationManager userCredentialsAuthenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/login").permitAll()
                    .usernameParameter("email")
                    .passwordParameter("password")
                    .successHandler(bySmsCodeAuthenticationHandler)
                .and()
                    .logout().permitAll();
        return http.build();
    }
}
