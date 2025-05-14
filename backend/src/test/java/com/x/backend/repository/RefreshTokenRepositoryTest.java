package com.x.backend.repository;

import com.x.backend.AbstractPostgreSQLTestContainer;
import com.x.backend.models.user.ApplicationUser;
import com.x.backend.models.user.auth.RefreshToken;
import com.x.backend.repositories.ApplicationUserRepository;
import com.x.backend.repositories.RefreshTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RefreshTokenRepositoryTest extends AbstractPostgreSQLTestContainer {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private ApplicationUserRepository applicationUserRepository;

    @AfterEach
    public void cleanUp() {
        refreshTokenRepository.deleteAll();
        applicationUserRepository.deleteAll();
    }

    @Test
    @DisplayName("should save and find refresh token by token value")
    void shouldFindByToken() {
        // given
        ApplicationUser user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("capellax@example.com");
        user.setUsername("capellax");
        user.setPassword("password");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setEnabled(true);
        ApplicationUser savedUser = applicationUserRepository.save(user);

        RefreshToken refreshToken = new RefreshToken(
                "sample-refresh-token",
                savedUser,
                Instant.now().plusSeconds(3600)
        );
        refreshTokenRepository.save(refreshToken);

        // when
        var optionalRefreshToken = refreshTokenRepository.findByToken("sample-refresh-token");

        // then
        assertThat(optionalRefreshToken).isPresent();
        assertThat(optionalRefreshToken.get().getUser().getId()).isEqualTo(savedUser.getId());
        assertThat(optionalRefreshToken.get().getToken()).isEqualTo("sample-refresh-token");
    }

    @Test
    @DisplayName("should delete refresh token by user")
    void shouldDeleteByUser() {
        // given
        ApplicationUser user = new ApplicationUser();
        user.setFirstName("Test");
        user.setLastName("User");
        user.setEmail("capellax@example.com");
        user.setUsername("capellax");
        user.setPassword("password");
        user.setDateOfBirth(LocalDate.of(2000, 1, 1));
        user.setEnabled(true);
        ApplicationUser savedUser = applicationUserRepository.save(user);

        RefreshToken refreshToken = new RefreshToken(
                "refresh-token-to-be-deleted",
                savedUser,
                Instant.now().plusSeconds(3600)
        );
        refreshTokenRepository.save(refreshToken);

        // when
        refreshTokenRepository.delete(refreshToken);

        // then
        var optionalRefreshToken = refreshTokenRepository.findByToken("refresh-token-to-be-deleted");
        assertThat(optionalRefreshToken).isNotPresent();
    }

}
