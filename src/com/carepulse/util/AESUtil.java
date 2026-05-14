package com.carepulse.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

// AES encryption helper used to store passwords in the database.
public class AESUtil {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    // Returns the AES key from the environment variable if set, otherwise the default key.
    private static String getSecretKey() {
        String key = System.getenv("CAREPULSE_AES_KEY");
        return (key != null && !key.trim().isEmpty()) ? key : "CarePulse@2024!!";
    }

    // Encrypts plain text and returns the result as a Base64 string.
    public static String encrypt(String plainText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(getSecretKey().getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted = cipher.doFinal(plainText.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    // Decrypts a Base64-encoded encrypted string back to plain text.
    public static String decrypt(String encryptedText) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(getSecretKey().getBytes("UTF-8"), ALGORITHM);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decoded = Base64.getDecoder().decode(encryptedText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, "UTF-8");
    }
}
