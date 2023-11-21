package com.example.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class SecurityUtil {
    private final static BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static BCryptPasswordEncoder getPasswordEncoder() {
        return PASSWORD_ENCODER;
    }

    public String encodePassword(String password) {
        return PASSWORD_ENCODER.encode(password);
    }

}
