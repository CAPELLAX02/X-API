package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseConflictException;

public class NicknameAlreadyInUseException extends BaseConflictException {
    public NicknameAlreadyInUseException(String nickname) {
        super("Nickname already in use: " + nickname);
    }
}
