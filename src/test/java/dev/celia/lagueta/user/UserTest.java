package dev.celia.lagueta.user;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class UserTest {
    @Test
    void testGetId() {
        User user = new User();
        user.setId(1L);
        assertEquals(1L, user.getId());

    }

    @Test
    void testGetPassword() {
        User user = new User();
        user.setPassword("password123");
        assertEquals("password123", user.getPassword());

    }

    @Test
    void testGetRole() {
        User user = new User();
        user.setRole("ADMIN");
        assertEquals("ADMIN", user.getRole());

    }

    @Test
    void testGetUsername() {
        User user = new User();
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());

    }

    @Test
    void testSetId() {
        User user = new User();
        user.setId(2L);
        assertEquals(2L, user.getId());

    }

    @Test
    void testSetPassword() {
        User user = new User();
        user.setPassword("newpassword456");
        assertEquals("newpassword456", user.getPassword());

    }

    @Test
    void testSetRole() {
        User user = new User();
        user.setRole("USER");
        assertEquals("USER", user.getRole());

    }

    @Test
    void testSetUsername() {
        User user = new User();
        user.setUsername("newuser");
        assertEquals("newuser", user.getUsername());

    }
}
