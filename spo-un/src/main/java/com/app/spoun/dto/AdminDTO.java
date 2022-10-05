package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AdminDTO {

    private Integer id;

    private String username;

    private String password;

    private Integer role_id;

}
