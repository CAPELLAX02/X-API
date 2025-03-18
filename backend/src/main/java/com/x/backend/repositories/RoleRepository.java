package com.x.backend.repositories;

import com.x.backend.models.entities.Role;
import com.x.backend.models.enums.RoleType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Optional<Role> findByAuthority(RoleType authority);
    boolean existsByAuthority(RoleType authority);

    @Query("SELECT COUNT(r) FROM Role r WHERE r.authority = :authority")
    long countByRoleType(RoleType authority);

}
