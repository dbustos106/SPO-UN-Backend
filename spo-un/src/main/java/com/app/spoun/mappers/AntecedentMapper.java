package com.app.spoun.mappers;

import com.app.spoun.dao.AntecedentDAO;
import com.app.spoun.dto.AntecedentDTO;
import org.mapstruct.*;

@Mapper
public interface AntecedentMapper {

    @Mapping(source = "patient.id", target = "patient_id")
    public AntecedentDTO antecedentDAOToAntecedentDTO(AntecedentDAO antecedentDAO);

    @Mapping(source = "patient_id", target = "patient.id")
    public AntecedentDAO antecedentDTOToAntecedentDAO(AntecedentDTO antecedentDTO);

}