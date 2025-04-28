package dev.celia.lagueta.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LoginRequestDTOTest {
    @Test
    void testGetPassword() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setPassword("password123");
        assertEquals("password123", dto.getPassword());
    }

    @Test
    void testGetUsername() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("admin");
        assertEquals("admin", dto.getUsername());
    }

    @Test
    void testSetPassword() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setPassword("newpassword123");
        assertEquals("newpassword123", dto.getPassword());

    }

    @Test
    void testSetUsername() {
        LoginRequestDTO dto = new LoginRequestDTO();
        dto.setUsername("newadmin");
        assertEquals("newadmin", dto.getUsername());

    }
}
