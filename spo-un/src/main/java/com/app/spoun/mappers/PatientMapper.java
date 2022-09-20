package com.app.spoun.mappers;

import com.app.spoun.domain.Patient;
import com.app.spoun.dto.PatientDTO;
import org.mapstruct.Mapper;

@Mapper
public interface PatientMapper {

    public PatientDTO patientToPatientDTO(Patient patient);

    public Patient patientDTOToPatient(PatientDTO patientDTO);

}
