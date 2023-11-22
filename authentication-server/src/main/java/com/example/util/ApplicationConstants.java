package com.example.util;

import lombok.experimental.UtilityClass;

/**
 * Util class to store final application constants and properties.
 * Using @UtilityClass makes all the properties final static;
 */
@UtilityClass
public class ApplicationConstants {
    public String APPLICATION_ID = "SPRING_SECURITY_MFA_ID";
    public String APPLICATION_BRAND = "SPRING_SECURITY_MFA_BRAND";
    public Integer SMS_CODE_TOKEN_TIME_LIFE_SECONDS = 30;

}
