package com.example.educoursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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


    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
}
