package com.x.backend.repositories;

import com.x.backend.AbstractPostgreSQLTestContainer;
import com.x.backend.models.ApplicationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest extends AbstractPostgreSQLTestContainer {

    @Autowired
    private UserRepository userRepository;

    private ApplicationUser applicationUser;

    @BeforeEach
    void setUp() {
        applicationUser = new ApplicationUser();
        applicationUser.setUsername("testuser123");
        applicationUser.setEmail("test@example.com");
        applicationUser.setPassword("testpass123");
        applicationUser.setFirstName("Test First Name");
        applicationUser.setLastName("Test Last Name");
        applicationUser.setPhoneNumber("+905318811876");

        userRepository.save(applicationUser);
    }

    @Test
    void shouldFindByUsername() {
        Optional<ApplicationUser> foundUser = userRepository.findByUsername("testuser123");

        assertTrue(foundUser.isPresent(), "User should be present");

        ApplicationUser retrievedUser = foundUser.get();
        assertEquals(applicationUser.getUsername(), retrievedUser.getUsername());
        assertEquals(applicationUser.getEmail(), retrievedUser.getEmail());
        assertEquals(applicationUser.getFirstName(), retrievedUser.getFirstName());
        assertEquals(applicationUser.getLastName(), retrievedUser.getLastName());
        assertEquals(applicationUser.getPhoneNumber(), retrievedUser.getPhoneNumber());
    }

    @Test
    void shouldReturnTrueIfEmailExists() {
        boolean exists = userRepository.existsByEmail("test@example.com");
        assertTrue(exists);
    }

    @Test
    void shouldReturnFalseIfEmailDoesNotExist() {
        boolean exists = userRepository.existsByEmail("testemail@example.com");
        assertFalse(exists);
    }

    @Test
    void shouldSaveAndRetrieveUser() {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername("newtestuser");
        newUser.setEmail("newtest@example.com");
        newUser.setPassword("newtestpass");
        newUser.setFirstName("New First Name");
        newUser.setLastName("New Last Name");
        newUser.setPhoneNumber("+905312345678");

        ApplicationUser savedUser = userRepository.save(newUser);
        Optional<ApplicationUser> foundUser = userRepository.findByUsername("newtestuser");

        assertTrue(foundUser.isPresent(), "User should be present in the database");

        ApplicationUser retrievedUser = foundUser.get();

        assertEquals(savedUser.getUserId(), retrievedUser.getUserId(), "User ID should match");
        assertEquals(savedUser.getUsername(), retrievedUser.getUsername(), "Username should match");
        assertEquals(savedUser.getEmail(), retrievedUser.getEmail(), "Email should match");
        assertEquals(savedUser.getFirstName(), retrievedUser.getFirstName(), "First name should match");
        assertEquals(savedUser.getLastName(), retrievedUser.getLastName(), "Last name should match");
        assertEquals(savedUser.getPhoneNumber(), retrievedUser.getPhoneNumber(), "Phone number should match");
    }

    @Test
    void shouldDeleteUserById() {
        ApplicationUser userToDelete = new ApplicationUser();
        userToDelete.setUsername("deletetestuser");
        userToDelete.setEmail("delete@example.com");
        userToDelete.setPassword("deletepass");
        userToDelete.setFirstName("Delete First Name");
        userToDelete.setLastName("Delete Last Name");
        userToDelete.setPhoneNumber("+905312345678");

        ApplicationUser savedUser = userRepository.save(userToDelete);
        Integer userId = savedUser.getUserId();

        userRepository.deleteById(userId);

        Optional<ApplicationUser> foundUser = userRepository.findById(userId);
        assertFalse(foundUser.isPresent(), "User should be deleted from the database");
    }

    @Test
    void shouldUpdateUser() {
        Optional<ApplicationUser> foundUser = userRepository.findByUsername("testuser123");
        assertTrue(foundUser.isPresent(), "User should exist before update");

        ApplicationUser userToUpdate = foundUser.get();
        userToUpdate.setEmail("updatedemail@example.com");

        userRepository.save(userToUpdate);

        Optional<ApplicationUser> updatedUser = userRepository.findByUsername("testuser123");
        assertTrue(updatedUser.isPresent(), "User should still exist after update");

        assertEquals("updatedemail@example.com", updatedUser.get().getEmail(), "Email should be updated");
    }

    @Test
    void shouldFindById() {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setUsername("idtestuser");
        newUser.setEmail("idtest@example.com");
        newUser.setPassword("idtestpass");
        newUser.setFirstName("ID Test First Name");
        newUser.setLastName("ID Test Last Name");
        newUser.setPhoneNumber("+905312345678");

        ApplicationUser savedUser = userRepository.save(newUser);

        Optional<ApplicationUser> foundUser = userRepository.findById(savedUser.getUserId());

        assertTrue(foundUser.isPresent(), "User should be found by ID");
        assertEquals(savedUser.getUsername(), foundUser.get().getUsername(), "Username should match");
    }

    @Test
    void shouldNotSaveNullUser() {
        assertThrows(InvalidDataAccessApiUsageException.class,
                () -> userRepository.save(null),
                "Saving null user should throw InvalidDataAccessApiUsageException");
    }

    @Test
    void shouldNotSaveDuplicateUsername() {
        ApplicationUser duplicateUser = new ApplicationUser();
        duplicateUser.setUsername("testuser123");
        duplicateUser.setEmail("another@example.com");
        duplicateUser.setPassword("pass");
        duplicateUser.setFirstName("Another First Name");
        duplicateUser.setLastName("Another Last Name");
        duplicateUser.setPhoneNumber("+905318812345");

        assertThrows(Exception.class, () ->
                        userRepository.save(duplicateUser),
                "Saving a user with a duplicate username should fail"
        );
    }

    @Test
    void shouldNotSaveDuplicateEmail() {
        ApplicationUser duplicateUser = new ApplicationUser();
        duplicateUser.setUsername("anotheruser");
        duplicateUser.setEmail("test@example.com");
        duplicateUser.setPassword("pass");
        duplicateUser.setFirstName("Another First Name");
        duplicateUser.setLastName("Another Last Name");
        duplicateUser.setPhoneNumber("+905318812345");

        assertThrows(Exception.class, () ->
                        userRepository.save(duplicateUser),
                "Saving a user with a duplicate email should fail"
        );
    }

    @Test
    void shouldReturnEmptyAfterUserDeletion() {
        Optional<ApplicationUser> foundUser = userRepository.findByUsername("testuser123");
        assertTrue(foundUser.isPresent(), "User should exist before deletion");

        userRepository.delete(foundUser.get());

        Optional<ApplicationUser> deletedUser = userRepository.findByUsername("testuser123");
        assertFalse(deletedUser.isPresent(), "User should not exist after deletion");
    }

    @Test
    void shouldReturnEmptyForNonExistentUserId() {
        Optional<ApplicationUser> foundUser = userRepository.findById(-1);
        assertFalse(foundUser.isPresent(), "Should return empty Optional for non-existent user ID");
    }

}
