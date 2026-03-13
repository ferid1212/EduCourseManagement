package com.example.educoursemanagementsystem.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CourseRequestDTO {
    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    String title;
    @Size(max = 2000, message = "Description cannot exceed 2000 characters")
    String description;
    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 hour")
    @Max(value = 1000, message = "Duration cannot exceed 1000 hours")
    Integer duration;
    @NotNull(message = "Price is required")
    @Digits(integer = 5, fraction = 2, message = "Price must have at most 5 digits and 2 decimal places")
    Double price;
}
