package com.example.educoursemanagementsystem.dto.response;

import com.example.educoursemanagementsystem.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class LessonResponse {
    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title;
    String videoURL;
    @Size(max = 4000, message = "Description cannot exceed 4000 characters")
    String content;
    LocalDateTime createAt;
    LocalDateTime updateAt;
    @NotNull(message = "Active status is required")
    Boolean isActive;
    Course course;
}
