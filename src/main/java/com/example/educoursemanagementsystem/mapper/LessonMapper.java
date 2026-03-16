package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public interface LessonMapper {
    LessonResponse toLessonResponse(Lesson lesson);
}
