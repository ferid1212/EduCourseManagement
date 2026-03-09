package com.example.educoursemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Data
@Builder
public class CourseResponseDTO {
     Long id;
     String title;
     String description;
     Double price;
     Integer duration;
     Boolean isActive;
     LocalDateTime create_at;
     LocalDateTime update_at;


}
