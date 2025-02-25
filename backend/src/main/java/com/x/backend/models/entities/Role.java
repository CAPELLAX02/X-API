package com.x.backend.models.entities;

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
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "authority", nullable = false, unique = true, length = 50)
    private RoleType authority;

    @Override
    public String getAuthority() {
        return this.authority.name();
    }

    public Role() {}

    public Role(Long id, RoleType authority) {
        this.id = id;
        this.authority = authority;
    }

    public void setAuthority(RoleType authority) {
        this.authority = authority;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
