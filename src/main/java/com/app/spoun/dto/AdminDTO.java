package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDTO {

    private Long id;

    private String email;

    private String password;

    private String verification_code;

    private boolean enabled;

    private Long role_id;

}
