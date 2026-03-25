package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.model.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.model.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public interface LessonMapper {
    LessonResponse toLessonResponse(Lesson lesson);
}
