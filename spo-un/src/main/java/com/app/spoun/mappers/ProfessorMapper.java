package com.app.spoun.mappers;

import com.app.spoun.dao.ProfessorDAO;
import com.app.spoun.dto.ProfessorDTO;
import org.mapstruct.Mapper;

@Mapper
public interface ProfessorMapper {

    public ProfessorDTO professorDAOToProfessorDTO(ProfessorDAO professorDAO);

    public ProfessorDAO professorDTOToProfessorDAO(ProfessorDTO professorDTO);
}