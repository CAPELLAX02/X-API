package com.x.backend.repositories;

import com.x.backend.models.entities.ApplicationUser;
import com.x.backend.models.entities.ValidAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ValidAccessTokenRepository extends JpaRepository<ValidAccessToken, Long> {

    List<ValidAccessToken> findAllByUser(ApplicationUser user);
    void deleteAllByUser(ApplicationUser user);
    boolean existsByJti(String jti);

}
