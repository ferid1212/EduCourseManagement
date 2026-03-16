package com.example.educoursemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
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
    @CreationTimestamp
    LocalDateTime createAt;
    @Column(name = "update_at")
    @UpdateTimestamp
    LocalDateTime updateAt;
    @Column(name = "is_active")
    Boolean isActive=true;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    Course course;

    @PrePersist
    public void prePersist() {
        if (isActive == null) {
            isActive = true;
        }
    }
}
