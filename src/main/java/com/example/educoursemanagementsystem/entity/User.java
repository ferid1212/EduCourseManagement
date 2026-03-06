package com.example.educoursemanagementsystem.entity;

import com.example.educoursemanagementsystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    String name;
    @Column(unique = true)
    String email;
    String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
