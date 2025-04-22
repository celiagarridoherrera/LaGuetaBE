package dev.celia.lagueta.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.celia.lagueta.user.User;
import dev.celia.lagueta.user.UserRepository;

@Configuration
public class AdminInitializer {

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            String adminUsername = System.getProperty("ADMIN_USERNAME", "admin");
            String adminPassword = System.getProperty("ADMIN_PASSWORD", "admin123");

            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User(null, adminUsername, encoder.encode(adminPassword), "ROLE_ADMIN");
                userRepository.save(admin);
                System.out.println("Administrador creado: " + adminUsername);
            }
        };
    }
}
