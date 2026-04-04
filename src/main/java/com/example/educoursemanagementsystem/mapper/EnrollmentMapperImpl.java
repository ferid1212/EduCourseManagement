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
                .studentId(enrollment.getStudent() != null ? enrollment.getStudent().getId() : null)
                .studentName(enrollment.getStudent() != null ? enrollment.getStudent().getName() : null)
                .studentSurname(enrollment.getStudent() != null ? enrollment.getStudent().getSurname() : null)
                .courseId(enrollment.getCourse() != null ? enrollment.getCourse().getId() : null)
                .courseTitle(enrollment.getCourse() != null ? enrollment.getCourse().getTitle() : null)
                .enrollmentDate(enrollment.getEnrollmentDate())
                .status(enrollment.getStatus())
                .createAt(enrollment.getCreateAt())
                .updateAt(enrollment.getUpdateAt())
                .isActive(enrollment.getIsActive())
                .isPaid(enrollment.getIsPaid())
                .build();
    }
}
