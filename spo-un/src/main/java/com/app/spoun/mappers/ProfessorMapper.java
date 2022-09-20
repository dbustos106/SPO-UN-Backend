package com.app.spoun.mappers;

import com.app.spoun.domain.Professor;
import com.app.spoun.dto.ProfessorDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProfessorMapper {

    public ProfessorDTO professorToProfessorDTO(Professor professor);

    public Professor professorDTOToProfessor(ProfessorDTO professorDTO);
}