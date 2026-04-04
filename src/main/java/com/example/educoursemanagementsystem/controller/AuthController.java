package com.example.educoursemanagementsystem.controller;

import com.example.educoursemanagementsystem.config.JwtService;
import com.example.educoursemanagementsystem.model.dto.request.AuthRequest;
import com.example.educoursemanagementsystem.model.dto.response.AuthResponse;
import com.example.educoursemanagementsystem.model.dto.request.RegisterRequest;
import com.example.educoursemanagementsystem.model.entity.User;
import com.example.educoursemanagementsystem.enums.Role;
import com.example.educoursemanagementsystem.model.entity.Student;
import com.example.educoursemanagementsystem.repository.StudentRepository;
import com.example.educoursemanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu hesab (email) artıq mövcuddur!");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.STUDENT);
        user.setIsActive(true);
        user.setPhone(request.getPhone());
        userRepository.save(user);

        // Also create a Student record
        Student student = new Student();
        student.setName(request.getFirstName());
        student.setSurname(request.getLastName());
        student.setEmail(request.getEmail());
        student.setAge(request.getAge());
        student.setPhone(request.getPhone());
        student.setIsActive(true);
        studentRepository.save(student);

        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).build());
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        return ResponseEntity.ok(AuthResponse.builder().token(jwtToken).build());
    }
}
