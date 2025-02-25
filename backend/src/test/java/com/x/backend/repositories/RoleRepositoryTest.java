package com.x.backend.repositories;

import com.x.backend.AbstractPostgreSQLTestContainer;
import com.x.backend.models.entities.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RoleRepositoryTest extends AbstractPostgreSQLTestContainer {

    @Autowired
    private RoleRepository roleRepository;

    private Role userRole;

    @BeforeEach
    void setUp() {
        userRole = new Role();
        userRole.setAuthority("USER");
        roleRepository.save(userRole);
    }

    @Test
    void shouldSaveAndRetrieveRole() {
        Role savedRole = roleRepository.save(userRole);

        Optional<Role> foundRole = roleRepository.findById(savedRole.getRoleId());

        assertTrue(foundRole.isPresent(), "Role should be present in the database");

        Role retrievedRole = foundRole.get();
        assertEquals(savedRole.getRoleId(), retrievedRole.getRoleId(), "Role ID should match");
        assertEquals(savedRole.getAuthority(), retrievedRole.getAuthority(), "Authority should match");
    }

    @Test
    void shouldFindByAuthority() {
        Optional<Role> foundRole = roleRepository.findByAuthority("USER");

        assertTrue(foundRole.isPresent(), "Role should be found by authority");
        assertEquals(userRole.getAuthority(), foundRole.get().getAuthority(), "Authority should match");
    }

    @Test
    void shouldReturnEmptyIfRoleDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findByAuthority("NON_EXISTENT_ROLE");

        assertFalse(foundRole.isPresent(), "Should return empty Optional for non-existent role");
    }

    @Test
    void shouldNotCreateDuplicateRoles() {
        long initialCount = roleRepository.count();

        Role duplicateRole = new Role();
        duplicateRole.setAuthority("USER");

        if (!roleRepository.existsByAuthority(duplicateRole.getAuthority())) {
            roleRepository.save(duplicateRole);
        }

        long finalCount = roleRepository.count();

        assertEquals(initialCount, finalCount, "There should be only one role in the database");
    }

    @Test
    void shouldDeleteRoleById() {
        roleRepository.deleteById(userRole.getRoleId());

        Optional<Role> foundRole = roleRepository.findById(userRole.getRoleId());

        assertFalse(foundRole.isPresent(), "Deleted role should not be found in the database");
    }

    @Test
    void shouldFindAllRoles() {
        Role adminRole = new Role();
        adminRole.setAuthority("ADMIN");
        roleRepository.save(adminRole);

        Iterable<Role> roles = roleRepository.findAll();
        long count = roleRepository.count();

        assertEquals(2, count, "There should be two roles in the database");
        assertTrue(roles.iterator().hasNext(), "Roles should be retrievable from the database");
    }

    @Test
    void shouldReturnEmptyWhenRoleIdDoesNotExist() {
        Optional<Role> foundRole = roleRepository.findById(9999);

        assertFalse(foundRole.isPresent(), "No role should be found for a non-existent ID");
    }

    @Test
    void shouldDeleteAllRoles() {
        roleRepository.deleteAll();

        long roleCount = roleRepository.count();
        assertEquals(0, roleCount, "Database should be empty after deleting all roles");
    }


}
