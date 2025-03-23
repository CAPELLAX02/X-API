package com.x.backend.exceptions.user;

public class NicknameAlreadyInUseException extends RuntimeException {
    public NicknameAlreadyInUseException(String nickname) {
        super("Nickname already in use: " + nickname);
    }
}
