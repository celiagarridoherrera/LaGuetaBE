package dev.celia.lagueta.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.*;

public class UserDetailsServiceImplTest {
    @Test
    void testLoadUserByUsernameFound() {

        UserRepository mockRepo = mock(UserRepository.class);
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(mockRepo);

        User user = new User(1L, "admin", "password123", "ADMIN");
        when(mockRepo.findByUsername("admin")).thenReturn(Optional.of(user));

        UserDetails userDetails = service.loadUserByUsername("admin");

        assertEquals("admin", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));

    }

    @Test
    void testLoadUserByUsernameNotFound() {

        UserRepository mockRepo = mock(UserRepository.class);
        UserDetailsServiceImpl service = new UserDetailsServiceImpl(mockRepo);

        when(mockRepo.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> {
            service.loadUserByUsername("unknown");
        });
    }
}
