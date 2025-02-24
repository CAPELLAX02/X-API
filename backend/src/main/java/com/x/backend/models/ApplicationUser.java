package com.x.backend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_username", columnList = "username", unique = true)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_email", columnNames = "email"),
                @UniqueConstraint(name = "uq_username", columnNames = "username")
        }
)
public class ApplicationUser implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Size(max = 100)
    private String email;

    @Column(name = "phone", nullable = false, unique = true, length = 15)
    @Size(min = 10, max = 15)
    private String phone;

    @Column(name = "dob", nullable = false)
    @Temporal(TemporalType.DATE)
    private LocalDateTime dateOfBirth;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "bio", length = 200)
    private String bio;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "profile_picture", referencedColumnName = "image_id")
    private Image profilePicture;

    @OneToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "banner_picture", referencedColumnName = "image_id")
    private Image bannerPicture;

    @Column(name = "is_verified_account", nullable = false)
    private boolean isVerifiedAccount = false;

    @Column(name = "is_private_account", nullable = false)
    private boolean isPrivateAccount = false;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @Column(name = "business", length = 100)
    private String business;

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "website_url", length = 120)
    private String websiteUrl;

    @Column(name = "display_dob", nullable = false)
    private Boolean displayDateOfBirth;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_relationships",
            joinColumns = @JoinColumn(name = "follower_id"),
            inverseJoinColumns = @JoinColumn(name = "following_id")
    )
    private Set<ApplicationUser> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<ApplicationUser> followers = new HashSet<>();

    /* SECURITY-RELATED FIELDS */

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> authorities = new HashSet<>();

    @Column(name = "enabled", nullable = false)
    private Boolean enabled = false;

    @JsonIgnore
    @Column(name = "verification_code")
    private String verificationCode;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    public ApplicationUser() {}

    public ApplicationUser(
            String email,
            Long id,
            String firstName,
            String lastName,
            String phone,
            LocalDateTime dateOfBirth,
            String username,
            String password,
            String bio,
            String nickname,
            LocalDateTime createdAt,
            Image profilePicture,
            Image bannerPicture,
            boolean isVerifiedAccount,
            boolean isPrivateAccount,
            Organization organization,
            String business,
            String location,
            String websiteUrl,
            Boolean displayDateOfBirth,
            Set<ApplicationUser> following,
            Set<ApplicationUser> followers,
            Set<Role> authorities,
            Boolean enabled,
            String verificationCode
    ) {
        this.email = email;
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.username = username;
        this.password = password;
        this.bio = bio;
        this.nickname = nickname;
        this.createdAt = createdAt;
        this.profilePicture = profilePicture;
        this.bannerPicture = bannerPicture;
        this.isVerifiedAccount = isVerifiedAccount;
        this.isPrivateAccount = isPrivateAccount;
        this.organization = organization;
        this.business = business;
        this.location = location;
        this.websiteUrl = websiteUrl;
        this.displayDateOfBirth = displayDateOfBirth;
        this.following = following;
        this.followers = followers;
        this.authorities = authorities;
        this.enabled = enabled;
        this.verificationCode = verificationCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ApplicationUser that = (ApplicationUser) o;
        return Objects.equals(id, that.id) || Objects.equals(email, that.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }

    @Override
    public String toString() {
        return "ApplicationUser{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", bio='" + bio + '\'' +
                ", nickname='" + nickname + '\'' +
                ", createdAt=" + createdAt +
                ", profilePicture=" + profilePicture +
                ", bannerPicture=" + bannerPicture +
                ", isVerifiedAccount=" + isVerifiedAccount +
                ", isPrivateAccount=" + isPrivateAccount +
                ", organization=" + organization +
                ", business='" + business + '\'' +
                ", location='" + location + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", displayDateOfBirth=" + displayDateOfBirth +
                ", following=" + following +
                ", followers=" + followers +
                ", authorities=" + authorities +
                ", enabled=" + enabled +
                ", verificationCode='" + verificationCode + '\'' +
                '}';
    }
}
