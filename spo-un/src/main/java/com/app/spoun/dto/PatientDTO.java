package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PatientDTO {

    private Long id;

    private String username;

    private String password;

    private String name;

    private String last_name;

    private String email;

    private String document_type;

    private String document_number;

    private Integer age;

    private String gender;

    private String blood_type;

    private String verification_code;

    private boolean enabled;

    private Long role_id;

}
