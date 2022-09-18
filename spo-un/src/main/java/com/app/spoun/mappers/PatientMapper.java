package com.app.spoun.mappers;

import com.app.spoun.dao.PatientDAO;
import com.app.spoun.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PatientMapper {

    public PatientDTO patientDAOToPatientDTO(PatientDAO patientDAO);

    public PatientDAO patientDTOToPatientDAO(PatientDTO patientDTO);

}
