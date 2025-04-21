package dev.celia.lagueta.auth;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();

        String testSecret = "mysupersecretkey1234567890mysupersecretkey1234567890";
        try {
            var field = JwtUtil.class.getDeclaredField("secret");
            field.setAccessible(true);
            field.set(jwtUtil, testSecret);
        } catch (Exception e) {
            fail("No se pudo establecer el secreto en JwtUtil: " + e.getMessage());
        }
    }

    @Test
    void testGenerateToken() {
        var authorities = List.of(new SimpleGrantedAuthority("ROLE_ADMIN"));
        var auth = new UsernamePasswordAuthenticationToken("admin", "password", authorities);
        String token = jwtUtil.generateToken(auth);
        assertNotNull(token);
        assertTrue(token.length() > 0);

    }
}
