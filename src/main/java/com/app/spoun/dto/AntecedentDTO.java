package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AntecedentDTO {

    private Long id;

    private String type;

    private String description;

    private Long patient_id;

}
