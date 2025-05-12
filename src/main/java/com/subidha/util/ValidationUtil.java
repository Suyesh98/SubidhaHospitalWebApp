
package com.subidha.util;

import java.util.regex.Pattern;

public class ValidationUtil {

    

   
    private static final Pattern PHONE_PATTERN = Pattern.compile("^(97|98)\\d{8}$");

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

   

     public static boolean isValidPhoneNumber(String phone) {
        if (isNullOrEmpty(phone)) return false;
        return PHONE_PATTERN.matcher(phone.replaceAll("[\\s\\-\\(\\)]", "")).matches(); 
    }

    public static boolean isValidPassword(String password) {
       
        return !isNullOrEmpty(password) && password.length() >= 8; 
    }
    
    
}