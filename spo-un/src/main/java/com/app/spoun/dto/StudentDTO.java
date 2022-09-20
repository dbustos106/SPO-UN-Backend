package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StudentDTO {

    private Integer id;

    private String username;

    private String password;

    private String name;

    private String document_type;

    private String document_number;

    private Integer Professor_id;

}