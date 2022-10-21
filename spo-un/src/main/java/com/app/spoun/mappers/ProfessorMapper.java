package com.app.spoun.mappers;

import com.app.spoun.domain.Professor;
import com.app.spoun.dto.ProfessorDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProfessorMapper {

    @Mapping(source = "role.id", target = "role_id")
    public ProfessorDTO professorToProfessorDTO(Professor professor);

    @Mapping(source = "role_id", target = "role.id")
    public Professor professorDTOToProfessor(ProfessorDTO professorDTO);

}