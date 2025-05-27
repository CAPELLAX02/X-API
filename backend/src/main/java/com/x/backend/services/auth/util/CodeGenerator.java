package com.x.backend.services.auth.util;

import java.util.concurrent.ThreadLocalRandom;

public class CodeGenerator {

    private static final int MIN = 10000;
    private static final int MAX = 99999;

    public static String generateVerificationCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
    }

    public static String generatePasswordRecoveryCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
    }

}
