package com.app.spoun.mappers;

import com.app.spoun.domain.Antecedent;
import com.app.spoun.dto.AntecedentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AntecedentMapper {

    @Mapping(source = "patient.id", target = "patient_id")
    public AntecedentDTO antecedentToAntecedentDTO(Antecedent antecedent);

    @Mapping(source = "patient_id", target = "patient.id")
    public Antecedent antecedentDTOToAntecedent(AntecedentDTO antecedentDTO);

}