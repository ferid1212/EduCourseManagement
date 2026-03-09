package com.example.educoursemanagementsystem.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class TeacherResponse {
    Long id;
    String name;
    String email;
    String surname;
    Integer age;
    LocalDateTime create_at;
    LocalDateTime update_at;
}
