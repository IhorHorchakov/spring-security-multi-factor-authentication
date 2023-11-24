package com.example.config;

import com.example.service.SmsCodeAuthenticationHandler;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static com.example.repository.entity.Authority.MFA_AUTHENTICATED;
import static com.example.repository.entity.Authority.READY_FOR_SMS_CODE_VERIFICATION;

@Configuration
public class SecurityConfig {
    @Autowired
    private UserService userService;
    @Autowired
    private SmsCodeAuthenticationHandler smsCodeAuthenticationHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
                .csrf().disable()
                .authorizeHttpRequests()
                    .requestMatchers("/verify-sms-code").hasAuthority(READY_FOR_SMS_CODE_VERIFICATION.getAuthority())
                    .requestMatchers("/home").hasAuthority(MFA_AUTHENTICATED.getAuthority())
                    .anyRequest().authenticated()
                .and()
                .formLogin()
                    .loginPage("/login").permitAll()
                    .successHandler(smsCodeAuthenticationHandler);
        return http.build();
    }
}
