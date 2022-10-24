package com.app.spoun.mappers;

import com.app.spoun.domain.Patient;
import com.app.spoun.dto.PatientDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "role.id", target = "role_id")
    public PatientDTO patientToPatientDTO(Patient patient);

    @Mapping(source = "role_id", target = "role.id")
    public Patient patientDTOToPatient(PatientDTO patientDTO);

}
