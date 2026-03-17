package com.example.educoursemanagementsystem.mapper;

import com.example.educoursemanagementsystem.dto.response.LessonResponse;
import com.example.educoursemanagementsystem.entity.Lesson;
import org.springframework.stereotype.Component;

@Component
public class LessonMapperImpl implements LessonMapper{
    @Override
    public LessonResponse toLessonResponse(Lesson lesson) {
        if(lesson==null){
            return null;
        }

        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .content(lesson.getContent())
                .videoURL(lesson.getVideoURL())
                .createAt(lesson.getCreateAt())
                .updateAt(lesson.getUpdateAt())
                .isActive(lesson.getIsActive())
                .courseName(lesson.getCourse().getTitle())
                .build();
    }
}
