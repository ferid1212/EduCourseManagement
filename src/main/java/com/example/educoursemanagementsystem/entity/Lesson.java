package com.example.educoursemanagementsystem.entity;

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
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String title;
    String videoURL;
    @Column(length = 5000)
    String content;
    LocalDateTime create_at;
    LocalDateTime update_at;
    Boolean isActive=true;


    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
