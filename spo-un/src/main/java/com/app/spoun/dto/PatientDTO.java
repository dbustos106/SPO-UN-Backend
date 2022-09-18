package com.app.spoun.dto;

import lombok.Data;

@Data
public class PatientDTO {

    private Integer id;

    private String username;

    private String password;

    private String name;

    private String email;

    private String document_type;

    private String document_number;

    private Integer age;

    private String gender;

    private String blood_type;

}
