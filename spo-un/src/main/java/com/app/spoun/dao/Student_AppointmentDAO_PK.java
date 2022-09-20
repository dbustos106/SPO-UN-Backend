package com.app.spoun.dao;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Student_AppointmentDAO_PK implements Serializable {

    private Integer student;

    private Integer appointment;

}
