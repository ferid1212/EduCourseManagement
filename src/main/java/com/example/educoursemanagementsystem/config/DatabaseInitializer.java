package com.example.educoursemanagementsystem.config;

import com.example.educoursemanagementsystem.model.entity.User;
import com.example.educoursemanagementsystem.enums.Role;
import com.example.educoursemanagementsystem.repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        // Drop the check constraint to allow PENDING status
        try {
            jdbcTemplate.execute("ALTER TABLE enrollments DROP CONSTRAINT IF EXISTS enrollments_status_check");
            System.out.println("Database constraint 'enrollments_status_check' dropped or already removed.");
        } catch (Exception e) {
            System.err.println("Error dropping DB constraint: " + e.getMessage());
        }

        if (!userRepository.existsByRole(Role.ADMIN)) {
            User admin = new User();
            admin.setEmail("admin@example.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFirstName("System");
            admin.setLastName("Admin");
            admin.setRole(Role.ADMIN);
            admin.setIsActive(true);
            admin.setPhone("994501234567");

            userRepository.save(admin);
            System.out.println("Avtomatik ADMIN yaradildi: admin@example.com / admin123");
        }
    }
}
