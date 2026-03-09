package com.example.educoursemanagementsystem.dto.request;

import lombok.Data;

@Data
public class CourseRequestDTO {
    String title;
    String description;
    Integer duration;
    Double price;
}
