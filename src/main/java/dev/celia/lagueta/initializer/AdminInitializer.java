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
            if (userRepository.findByUsername("admin").isEmpty()) {
                User admin = new User(null, "admin", encoder.encode("admin123"), "ADMIN");
                userRepository.save(admin);
                System.out.println("¡Administrador creado!");
            }
        };
    }
}
