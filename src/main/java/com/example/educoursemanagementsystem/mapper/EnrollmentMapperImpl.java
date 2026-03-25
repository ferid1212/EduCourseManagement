package com.example.educoursemanagementsystem.mapper;


import com.example.educoursemanagementsystem.model.dto.response.EnrollmentResponse;
import com.example.educoursemanagementsystem.model.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapperImpl implements EnrollmentMapper {
    @Override
    public EnrollmentResponse toEnrollmentResponse(Enrollment enrollment) {
        if(enrollment==null){
            return null;
        }

        return EnrollmentResponse.builder()
                .id(enrollment.getId())
                .studentId(enrollment.getStudent().getId())
                .studentName(enrollment.getStudent().getName())
                .studentSurname(enrollment.getStudent().getSurname())
                .courseId(enrollment.getCourse().getId())
                .courseTitle(enrollment.getCourse().getTitle())
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .createAt(enrollment.getCreateAt())
                .updateAt(enrollment.getUpdateAt())
                .isActive(enrollment.getIsActive())
                .build();
    }
}
