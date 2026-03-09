package com.example.educoursemanagementsystem.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Teacher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String email;
    String surname;
    Integer age;
    LocalDateTime create_at;
    LocalDateTime update_at;
    Boolean isActive=true;



    @ManyToOne
    @JoinColumn(name = "course_id")
    Course course;


}
