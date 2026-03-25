package com.example.educoursemanagementsystem.model.dto.response;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CourseDetailsResponseDTO {
    @NotNull(message = "ID is required")
    @Positive(message = "ID must be positive")
    Long id;
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title;
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String description;
    @NotNull(message = "Price is required")
    @Digits(integer = 5, fraction = 2, message = "Price must have at most 5 digits and 2 decimal places")
    Double price;
    @NotNull(message = "Duration is required")
    @Positive(message = "Duration must be positive")
    Integer duration;
    List<TeacherResponse> teachers;
    LocalDateTime create_at;
    LocalDateTime update_at;
}
