package com.app.spoun.dao;

import lombok.Data;

import java.io.Serializable;

@Data
public class Student_AppointmentPK implements Serializable {

    private StudentDAO student;

    private AppointmentDAO appointment;

}
