package com.example.educoursemanagementsystem.controller;

import com.example.educoursemanagementsystem.dto.RegisterRequest;
import com.example.educoursemanagementsystem.entity.Teacher;
import com.example.educoursemanagementsystem.entity.Course;
import com.example.educoursemanagementsystem.entity.User;
import com.example.educoursemanagementsystem.enums.Role;
import com.example.educoursemanagementsystem.repository.UserRepository;
import com.example.educoursemanagementsystem.repository.TeacherRepository;
import com.example.educoursemanagementsystem.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;

    @PostMapping("/create-teacher")
    public ResponseEntity<String> createTeacher(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Bu hesab (email) artıq mövcuddur!");
        }
        
        // 1. Create Login Details (Users Table)
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.TEACHER);
        user.setIsActive(true);
        userRepository.save(user);

        // 2. Create Teacher Details (Teacher Table)
        Teacher teacherEntity = new Teacher();
        teacherEntity.setName(request.getFirstName());
        teacherEntity.setSurname(request.getLastName());
        teacherEntity.setEmail(request.getEmail());
        teacherEntity.setAge(request.getAge());
        
        if (request.getCourseId() != null) {
            courseRepository.findById(request.getCourseId()).ifPresent(teacherEntity::setCourse);
        }
        
        teacherRepository.save(teacherEntity);

        return ResponseEntity.ok("Teacher hər iki cədvəldə yaddaşda saxlanıldı və kursa təyin edildi.");
    }
}
