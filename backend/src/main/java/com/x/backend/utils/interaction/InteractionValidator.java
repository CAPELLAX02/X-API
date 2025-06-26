package com.x.backend.utils.interaction;

import com.x.backend.services.user.block.UserBlockService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
public class InteractionValidator {

    private final UserBlockService userBlockService;

    public InteractionValidator(UserBlockService userBlockService) {
        this.userBlockService = userBlockService;
    }

    public void validateInteractionAllowed(String actorUsername, String targetUsername) {
        if (userBlockService.isUserBlocked(actorUsername, targetUsername).getData()) {
            throw new AccessDeniedException("You cannot interact with this user.");
        }
    }

}
