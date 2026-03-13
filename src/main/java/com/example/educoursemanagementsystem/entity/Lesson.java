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
    @Column(nullable = false)
    String title;
    @Column(name = "video_url")
    String videoURL;
    @Column(length = 5000)
    String content;
    @Column(name = "create_at")
    LocalDateTime createAt;
    @Column(name = "update_at")
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;


    @ManyToOne()
    @JoinColumn(name = "course_id", nullable = false)
    Course course;
}
