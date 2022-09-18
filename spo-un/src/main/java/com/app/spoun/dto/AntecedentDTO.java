package com.app.spoun.dto;

import com.app.spoun.dao.PatientDAO;
import lombok.Data;

import javax.persistence.*;

@Data
public class AntecedentDTO {

    private Integer id;

    private String type;

    private String description;

    private Integer patient_id;

}
