package com.x.backend.exceptions.user;

import com.x.backend.exceptions.BaseNotFoundException;

public class PrivacySettingsBaseNotFoundException extends BaseNotFoundException {
    public PrivacySettingsBaseNotFoundException() {
        super("Privacy Settings not found.");
    }
}
