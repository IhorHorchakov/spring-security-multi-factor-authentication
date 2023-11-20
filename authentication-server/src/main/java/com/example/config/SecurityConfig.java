package com.example.config;

import com.example.service.SmsCodeAuthenticationHandlerService;
import com.example.service.UserService;
import com.example.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsCodeAuthenticationHandlerService smsCodeAuthenticationHandlerService;
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return SecurityUtil.getPasswordEncoder();
    }

    @Bean
    public AuthenticationManager userCredentialsAuthenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authProvider);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin()
                    .loginPage("/login").permitAll()
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .successHandler(smsCodeAuthenticationHandlerService)
                .and()
                    .logout().permitAll();
        return http.build();
    }
}
