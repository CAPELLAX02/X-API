package com.x.backend.repositories;

import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.UserBlock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserBlockRepository extends BaseRepository<UserBlock, Long> {

    Optional<UserBlock> findByBlockingUserAndBlockedUser(ApplicationUser blocker, ApplicationUser blocked);

    List<UserBlock> findAllByBlockingUser(ApplicationUser user);

    List<UserBlock> findAllByBlockedUser(ApplicationUser user);

    boolean existsByBlockingUserAndBlockedUser(ApplicationUser blocker, ApplicationUser blocked);

}
