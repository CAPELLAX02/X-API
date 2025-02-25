package com.x.backend.models.entities;

import com.x.backend.models.AbstractBaseEntity;
import com.x.backend.models.enums.RoleType;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(
        name = "roles",
        indexes = {
                @Index(name = "idx_role_authority", columnList = "authority", unique = true)
        }
)
public class Role extends AbstractBaseEntity implements GrantedAuthority {

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false, unique = true, length = 50)
    private RoleType authority;

    @Override
    public String getAuthority() {
        return this.authority.name();
    }

    public Role() {}

    public Role(RoleType authority) {
        this.authority = authority;
    }

    public void setAuthority(RoleType authority) {
        this.authority = authority;
    }

}
