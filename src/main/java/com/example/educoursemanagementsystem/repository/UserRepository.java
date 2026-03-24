package com.example.educoursemanagementsystem.repository;

import com.example.educoursemanagementsystem.entity.User;
import com.example.educoursemanagementsystem.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByRole(Role role);
}
