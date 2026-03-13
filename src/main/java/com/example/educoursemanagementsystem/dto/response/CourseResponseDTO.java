package com.example.educoursemanagementsystem.dto.response;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class CourseResponseDTO {
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
     @NotNull(message = "Active status is required")
     Boolean isActive;
     LocalDateTime create_at;
     LocalDateTime update_at;


}
