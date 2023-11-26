package com.example.util;

/**
 * Util class to store final application constants and properties
 */
public final class ApplicationConstants {
    public static final String APPLICATION_ID = "SPRING_SECURITY_MFA_ID";
    public static final String APPLICATION_BRAND = "SPRING_SECURITY_MFA_BRAND";
    public static final Integer SMS_CODE_TOKEN_TIME_LIFE_SECONDS = 60;
    public static final Integer SMS_CODE_TOKEN_MAX_VERIFICATION_ATTEMPTS_ALLOWED= 2;

    private ApplicationConstants() {}
}
