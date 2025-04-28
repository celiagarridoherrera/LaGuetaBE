package dev.celia.lagueta.initializer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import dev.celia.lagueta.user.User;
import dev.celia.lagueta.user.UserRepository;

@Configuration
public class AdminInitializer {

    @Value("${ADMIN_USERNAME}")
    private String adminUsername;
    @Value("${ADMIN_PASSWORD}")
    private String adminPassword;

    @Bean
    CommandLineRunner initAdmin(UserRepository userRepository, BCryptPasswordEncoder encoder) {
        return args -> {
            if (userRepository.findByUsername(adminUsername).isEmpty()) {
                User admin = new User(null, adminUsername, encoder.encode(adminPassword), "ROLE_ADMIN");
                userRepository.save(admin);
                System.out.println("Administrador creado: " + adminUsername);
            } else {
                System.out.println("El usuario admin " + adminUsername + " ya existe.");
            }
        };
    }
}
