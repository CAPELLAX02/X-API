package com.x.backend.services.auth.util;

import org.springframework.stereotype.Component;

import java.util.concurrent.ThreadLocalRandom;

@Component
public class CodeGenerator {

    private static final int MIN = 10000;
    private static final int MAX = 99999;

    public String generateVerificationCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
    }

    public String generatePasswordRecoveryCode() {
        return String.valueOf(ThreadLocalRandom.current().nextInt(MIN, MAX + 1));
    }

}
