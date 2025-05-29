package com.x.backend.services.auth.util;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.concurrent.ThreadLocalRandom;

public class SecureCodeManager {

    private static final int MIN = 100000;
    private static final int MAX = 999999;

    public static String generateCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
    }

    public static String encode(String rawCode, PasswordEncoder encoder) {
        return encoder.encode(rawCode);
    }

    public static boolean matches(String rawCode, String hashedCode, PasswordEncoder encoder) {
        return encoder.matches(rawCode, hashedCode);
    }

}
