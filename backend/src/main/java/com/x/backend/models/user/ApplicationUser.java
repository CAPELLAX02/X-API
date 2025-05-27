package com.x.backend.models.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.x.backend.models.image.Image;
import com.x.backend.models.user.auth.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "users",
        indexes = {
                @Index(name = "idx_user_email", columnList = "email", unique = true),
                @Index(name = "idx_user_username", columnList = "username", unique = true),
                @Index(name = "idx_user_nickname", columnList = "nickname", unique = true)
        },
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_email", columnNames = "email"),
                @UniqueConstraint(name = "uq_username", columnNames = "username"),
                @UniqueConstraint(name = "uq_nickname", columnNames = "nickname")
        }
)
public class ApplicationUser implements UserDetails, Principal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Size(max = 100)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "dob", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "username", nullable = false, updatable = false, unique = true, length = 50)
    private String username;

    @JsonIgnore
    @Column(name = "password")
    private String password;

    @Column(name = "bio", length = 200)
    private String bio;

    @Column(name = "nickname", length = 50, unique = true)
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

    @Column(name = "location", length = 100)
    private String location;

    @Column(name = "website_url", length = 120)
    private String websiteUrl;

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

    @Column(name = "enabled", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean enabled;

    @JsonIgnore
    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "verification_code_expiry")
    private Instant verificationCodeExpiry;

    @JsonIgnore
    @Column(name = "password_recovery_code")
    private String passwordRecoveryCode;

    @Column(name = "password_recovery_code_expiry")
    private Instant passwordRecoveryCodeExpiry;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<>(this.authorities);
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
            Long id,
            String email,
            String firstName,
            String lastName,
            String phone,
            LocalDate dateOfBirth,
            String username,
            String password,
            String bio,
            String nickname,
            LocalDateTime createdAt,
            Image profilePicture,
            Image bannerPicture,
            boolean isVerifiedAccount,
            boolean isPrivateAccount,
            String location,
            String websiteUrl,
            Set<ApplicationUser> following,
            Set<ApplicationUser> followers,
            Set<Role> authorities,
            boolean enabled,
            String verificationCode,
            Instant verificationCodeExpiry,
            String passwordRecoveryCode,
            Instant passwordRecoveryCodeExpiry
    ) {
        this.id = id;
        this.email = email;
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
        this.location = location;
        this.websiteUrl = websiteUrl;
        this.following = following;
        this.followers = followers;
        this.authorities = authorities;
        this.enabled = enabled;
        this.verificationCode = verificationCode;
        this.verificationCodeExpiry = verificationCodeExpiry;
        this.passwordRecoveryCode = passwordRecoveryCode;
        this.passwordRecoveryCodeExpiry = passwordRecoveryCodeExpiry;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Image getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(Image profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Image getBannerPicture() {
        return bannerPicture;
    }

    public void setBannerPicture(Image bannerPicture) {
        this.bannerPicture = bannerPicture;
    }

    public boolean isVerifiedAccount() {
        return isVerifiedAccount;
    }

    public void setVerifiedAccount(boolean verifiedAccount) {
        isVerifiedAccount = verifiedAccount;
    }

    public boolean isPrivateAccount() {
        return isPrivateAccount;
    }

    public void setPrivateAccount(boolean privateAccount) {
        isPrivateAccount = privateAccount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public Set<ApplicationUser> getFollowing() {
        return following;
    }

    public void setFollowing(Set<ApplicationUser> following) {
        this.following = following;
    }

    public Set<ApplicationUser> getFollowers() {
        return followers;
    }

    public void setFollowers(Set<ApplicationUser> followers) {
        this.followers = followers;
    }

    public void setAuthorities(Set<Role> authorities) {
        this.authorities = authorities;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public Instant getVerificationCodeExpiry() {
        return verificationCodeExpiry;
    }

    public void setVerificationCodeExpiry(Instant verificationCodeExpiry) {
        this.verificationCodeExpiry = verificationCodeExpiry;
    }

    public String getPasswordRecoveryCode() {
        return passwordRecoveryCode;
    }

    public void setPasswordRecoveryCode(String passwordRecoveryCode) {
        this.passwordRecoveryCode = passwordRecoveryCode;
    }

    public Instant getPasswordRecoveryCodeExpiry() {
        return passwordRecoveryCodeExpiry;
    }

    public void setPasswordRecoveryCodeExpiry(Instant passwordRecoveryCodeExpiry) {
        this.passwordRecoveryCodeExpiry = passwordRecoveryCodeExpiry;
    }

    @Override
    public String getName() {
        return username;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

}
