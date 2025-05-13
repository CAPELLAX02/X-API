package com.x.backend.repositories;

import com.x.backend.models.user.user.UserBlock;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserBlockRepository extends BaseRepository<UserBlock, Long> {

    Optional<UserBlock> findByBlockingUserIdAndBlockedUserId(Long blockerId, Long blockedId);

    long countByBlockingUserId(Long userId);
}
