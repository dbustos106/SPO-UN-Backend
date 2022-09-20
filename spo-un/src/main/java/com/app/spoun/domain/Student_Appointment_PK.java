package com.app.spoun.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Student_Appointment_PK implements Serializable {

    private Integer student;

    private Integer appointment;

}
