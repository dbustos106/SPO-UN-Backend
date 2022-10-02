package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AntecedentDTO {

    private Integer id;

    private String type;

    private String description;

    private Integer patient_id;

}
