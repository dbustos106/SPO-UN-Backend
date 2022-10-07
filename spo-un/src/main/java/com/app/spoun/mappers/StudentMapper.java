package com.app.spoun.mappers;

import com.app.spoun.domain.Student;
import com.app.spoun.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {

    @Mapping(source = "professor.id", target = "professor_id")
    @Mapping(source = "role.id", target = "role_id")
    public StudentDTO studentToStudentDTO(Student student);

    @Mapping(source = "professor_id", target = "professor.id")
    @Mapping(source = "role_id", target = "role.id")
    public Student studentDTOToStudent(StudentDTO studentDTO);

}
