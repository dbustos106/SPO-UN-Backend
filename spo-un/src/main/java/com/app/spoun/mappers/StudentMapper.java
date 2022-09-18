package com.app.spoun.mappers;

import com.app.spoun.dao.StudentDAO;
import com.app.spoun.dto.StudentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {

    @Mapping(source = "professor.id", target = "professor_id")
    public StudentDTO studentDAOToStudentDTO(StudentDAO studentDAO);

    @Mapping(source = "professor_id", target = "professor.id")
    public StudentDAO studentDTOToStudentDAO(StudentDTO studentDTO);

}
