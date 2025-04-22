package dev.celia.lagueta.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class LoginResponseDTOTest {
    @Test
    void testGetRole() {
        LoginResponseDTO dto = new LoginResponseDTO("token123", "admin", "ADMIN");
        assertEquals("ADMIN", dto.getRole());
    }

    @Test
    void testGetToken() {
        LoginResponseDTO dto = new LoginResponseDTO("token123", "admin", "ADMIN");
        assertEquals("token123", dto.getToken());
    }

    @Test
    void testGetUsername() {
        LoginResponseDTO dto = new LoginResponseDTO("token123", "admin", "ADMIN");
        assertEquals("admin", dto.getUsername());
    }
}
