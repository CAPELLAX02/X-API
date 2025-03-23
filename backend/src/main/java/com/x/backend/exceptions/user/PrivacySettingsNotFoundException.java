package com.x.backend.exceptions.user;

import com.x.backend.exceptions.NotFoundException;

public class PrivacySettingsNotFoundException extends NotFoundException {
    public PrivacySettingsNotFoundException() {
        super("Privacy Settings not found.");
    }
}
