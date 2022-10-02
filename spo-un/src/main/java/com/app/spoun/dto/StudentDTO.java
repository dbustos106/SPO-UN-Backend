package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentDTO {

    private Integer id;

    private String username;

    private String password;

    private String name;

    private String document_type;

    private String document_number;

    private Integer professor_id;

    private Integer role_id;

}