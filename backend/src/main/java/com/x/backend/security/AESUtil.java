package com.x.backend.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class for AES-256 CBC encryption and decryption,
 * using a 32-char ASCII passphrase for the key
 * and a 16-char ASCII string for the IV.
 */
public class AESUtil {

    private static final String SECRET_KEY_ASCII = "THIS_IS_32_CHARS_EXAMPLE_1234_XY";
    private static final String IV_ASCII = "16_BYTE_IV_PASS!";

    private static final String ALGORITHM = "AES";
    private static final String AES_TRANSFORMATION = "AES/CBC/PKCS5Padding";

    /**
     * Create a 256-bit SecretKey (ASCII passphrase => 32 bytes).
     */
    public static SecretKey getSecretKey() {
        byte[] keyBytes = SECRET_KEY_ASCII.getBytes(StandardCharsets.US_ASCII);
        // (Optional) validate length == 32
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * Create a 128-bit IV (ASCII => 16 bytes).
     */
    public static IvParameterSpec getIv() {
        byte[] ivBytes = IV_ASCII.getBytes(StandardCharsets.US_ASCII);
        // (Optional) validate length == 16
        return new IvParameterSpec(ivBytes);
    }

    /**
     * Encrypts a plaintext string using AES-256 CBC, returns Base64 ciphertext.
     */
    public static String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(), getIv());
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    /**
     * Decrypts a Base64 ciphertext using AES-256 CBC.
     */
    public static String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), getIv());
        byte[] decoded = Base64.getDecoder().decode(cipherText);
        byte[] decrypted = cipher.doFinal(decoded);
        return new String(decrypted, StandardCharsets.UTF_8);
    }
}
