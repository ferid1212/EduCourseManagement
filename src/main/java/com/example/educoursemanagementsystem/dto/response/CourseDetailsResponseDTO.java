package com.example.educoursemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
@Builder
public class CourseDetailsResponseDTO {
    Long id;
    String title;
    String description;
    Double price;
    Integer duration;
    List<TeacherResponse> teachers;
    LocalDateTime create_at;
    LocalDateTime update_at;
}
