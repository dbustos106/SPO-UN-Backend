package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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

    private Integer role_id;

}
