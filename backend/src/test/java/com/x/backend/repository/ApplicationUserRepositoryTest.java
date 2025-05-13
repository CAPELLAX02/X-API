package com.x.backend.repository;

import com.x.backend.AbstractPostgreSQLTestContainer;
import com.x.backend.models.user.user.ApplicationUser;
import com.x.backend.repositories.ApplicationUserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ApplicationUserRepositoryTest extends AbstractPostgreSQLTestContainer {

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    public ApplicationUser createUser(String suffix, boolean isVerified) {
        ApplicationUser user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("user_" + suffix + "@example.com");
        user.setUsername("username_" + suffix);
        user.setPhone("555000" + suffix);
        user.setPassword("password");
        user.setBio("Test bio for user " + suffix);
        user.setNickname("nick_" + suffix);
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setVerifiedAccount(isVerified);
        user.setEnabled(true);
        return applicationUserRepository.save(user);
    }


    @AfterEach
    void cleanUp() {
        applicationUserRepository.deleteAll();
    }

    @Test
    @DisplayName("existsByEmail should return true when email exists")
    void existsByEmail_shouldReturnTrue() {
        ApplicationUser user = createUser("a01", true);
        boolean existsByEmail = applicationUserRepository.existsByEmail(user.getEmail());
        assertThat(existsByEmail).isTrue();
    }

    @Test
    @DisplayName("existsByUsername should return true when username exists")
    void existsByUsername_shouldReturnTrue() {
        ApplicationUser user = createUser("a02", true);
        boolean existsByUsername = applicationUserRepository.existsByUsername(user.getUsername());
        assertThat(existsByUsername).isTrue();
    }

    @Test
    @DisplayName("existsByPhone should return true when phone exists")
    void existsByPhone_shouldReturnTrue() {
        ApplicationUser user = createUser("a03", true);
        boolean existsByPhone = applicationUserRepository.existsByPhone(user.getPhone());
        assertThat(existsByPhone).isTrue();
    }

    @Test
    @DisplayName("existsByNickname should return true when nickname exists")
    void existsByNickname_shouldReturnTrue() {
        ApplicationUser user = createUser("a04", true);
        boolean existsByNickname = applicationUserRepository.existsByNickname(user.getNickname());
        assertThat(existsByNickname).isTrue();
    }

    @Test
    @DisplayName("findByEmail should return user")
    void findByEmail_shouldReturnUser() {
        ApplicationUser user = createUser("a05", true);
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByEmail(user.getEmail());
        assertThat(userOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByUsername should return user")
    void findByUsername_shouldReturnUser() {
        ApplicationUser user = createUser("a06", true);
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByUsername(user.getUsername());
        assertThat(userOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByPhone should return user")
    void findByPhone_shouldReturnUser() {
        ApplicationUser user = createUser("a07", true);
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByPhone(user.getPhone());
        assertThat(userOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findByNickname should return user")
    void findByNickname_shouldReturnUser() {
        ApplicationUser user = createUser("a08", true);
        Optional<ApplicationUser> userOptional = applicationUserRepository.findByNickname(user.getNickname());
        assertThat(userOptional.isPresent()).isTrue();
    }

    @Test
    @DisplayName("findAllByNicknameContainingIgnoreCase should return matching users")
    void findAllByNicknameContainingIgnoreCase_shouldReturnResults() {
        createUser("capellax_dev", true);
        createUser("CapEllAx02", true);
        List<ApplicationUser> results = applicationUserRepository.findAllByNicknameContainingIgnoreCase("capellax");
        assertThat(results).hasSize(2);
    }

}
