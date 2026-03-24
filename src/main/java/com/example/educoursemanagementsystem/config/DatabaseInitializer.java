package com.example.educoursemanagementsystem.config;

import com.example.educoursemanagementsystem.entity.User;
import com.example.educoursemanagementsystem.enums.Role;
import com.example.educoursemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);

            userRepository.save(admin);
            System.out.println("Avtomatik ADMIN yaradildi: admin@example.com / admin123");
        }
    }
}
