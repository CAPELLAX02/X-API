package com.x.backend.services.user;

import com.x.backend.models.entities.ApplicationUser;

public interface UserService {

    ApplicationUser getUserByUsername(String username);

}
