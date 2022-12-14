package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProfessorDTO {

    private Long id;

    private String email;

    private String password;

    private String name;

    private String last_name;

    private String document_type;

    private String document_number;

    private String verification_code;

    private boolean enabled;

    private Long role_id;

}