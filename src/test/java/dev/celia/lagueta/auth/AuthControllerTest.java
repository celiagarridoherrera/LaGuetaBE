package dev.celia.lagueta.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("user", "password");
        String expectedToken = "fake-jwt-token";
        String expectedRole = "ROLE_USER";
        
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn(expectedToken);
        when(authentication.getAuthorities()).thenReturn(List.of(() -> expectedRole));

        // Act
        LoginResponseDTO response = authController.login(request);

        // Assert
        assertEquals(expectedToken, response.getToken());
        assertEquals("user", response.getUsername());
        assertEquals(expectedRole, response.getRole());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("user", "wrong-password");

        when(authenticationManager.authenticate(any())).thenThrow(new RuntimeException("Invalid credentials"));

        // Act & Assert
        try {
            authController.login(request);
            fail("Expected an exception to be thrown");
        } catch (Exception e) {
            assertEquals("Invalid credentials", e.getMessage());
        }
    }

    @Test
    void testLoginNoRole() {
        // Arrange
        LoginRequestDTO request = new LoginRequestDTO("user", "password");
        String expectedToken = "fake-jwt-token";
        String expectedRole = "ROLE_UNKNOWN"; // Default role when no authorities are found

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtUtil.generateToken(authentication)).thenReturn(expectedToken);
        when(authentication.getAuthorities()).thenReturn(List.of()); // No roles


        LoginResponseDTO response = authController.login(request);

        assertEquals(expectedToken, response.getToken());
        assertEquals("user", response.getUsername());
        assertEquals(expectedRole, response.getRole());
    }
}
