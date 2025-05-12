package com.subidha.util;

import java.security.SecureRandom;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.util.Base64;

public class PasswordUtil {

	
	 

    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH = 128;  

    
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

   
    public static String hashPassword(String password, String salt) {
        try {
            
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            char[] passwordChars = password.toCharArray();

            
            PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, ITERATIONS, KEY_LENGTH);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();

            
            return salt + "$" + Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static boolean verifyPassword(String inputPassword, String storedPassword) {
        try {
            String[] parts = storedPassword.split("\\$");
            if (parts.length != 2) return false;
    
            String salt = parts[0];
            String storedHash = parts[1];
    
            String hashedInput = hashPassword(inputPassword, salt);
            String inputHash = hashedInput.split("\\$")[1];
    
            return storedHash.equals(inputHash);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
