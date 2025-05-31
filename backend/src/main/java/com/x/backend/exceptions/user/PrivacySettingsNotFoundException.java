package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class PrivacySettingsNotFoundException extends BaseNotFoundException {
    public PrivacySettingsNotFoundException() {
        super("Privacy Settings not found.");
    }
}
