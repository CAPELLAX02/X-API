package com.x.backend.repositories;

import com.x.backend.models.entities.ApplicationUser;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicationUserRepository extends BaseRepository<ApplicationUser, Long> {

    boolean existsByEmail(String email);
    boolean existsByUsername(String username);

    Optional<ApplicationUser> findByUsername(String username);

    @Query(value = "SELECT * FROM x_db.public.users ORDER BY created_at DESC LIMIT :limit", nativeQuery = true)
    List<ApplicationUser> findNewestUsers(@Param("limit") int limit);

    @Query("SELECT COUNT(u) FROM ApplicationUser u")
    long countAllUsers();

    @Query("SELECT u FROM ApplicationUser u WHERE u.isVerifiedAccount = true ORDER BY u.createdAt DESC")
    List<ApplicationUser> findVerifiedUsers();

    @EntityGraph(attributePaths = {"following", "followers"})
    @Query("SELECT u FROM ApplicationUser u WHERE u.id = :id")
    Optional<ApplicationUser> findUserWithFollowersAndFollowing(@Param("id") Long id);

}
