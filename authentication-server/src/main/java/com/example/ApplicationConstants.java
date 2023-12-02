package com.example;

/**
 * Util class to store final application constants and properties
 */
public final class ApplicationConstants {
    public static final String APPLICATION_BRAND = "SPRING_SECURITY_MFA_VONAGE";
    public static final Integer SMS_CODE_TOKEN_TIME_LIFE_SECONDS = 3 * 60;
    public static final Integer SMS_CODE_TOKEN_MAX_VERIFICATION_ATTEMPTS_ALLOWED= 2;

    private ApplicationConstants() {}
}
