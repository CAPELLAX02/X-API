package com.x.backend.exceptions.user;

import com.x.backend.exceptions.AlreadyExistsException;

public class NicknameAlreadyInUseException extends AlreadyExistsException {
    public NicknameAlreadyInUseException(String nickname) {
        super("Nickname already in use: " + nickname);
    }
}
