package com.example.educoursemanagementsystem.entity;

import com.example.educoursemanagementsystem.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long Id;
    String name;
    @Column(unique = true)
    String email;
    String password;
    LocalDateTime create_at;
    LocalDateTime update_at;
    Boolean isActive=true;
    @Enumerated(EnumType.STRING)
     Role role;
}
