package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@IdClass(value = Student_AppointmentPK.class)
@Table(name = "student_appointment", schema = "spo-un")
public class Student_AppointmentDAO {

    @Id
    @JoinColumn(name = "student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentDAO student;

    @Id
    @JoinColumn(name = "appointment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppointmentDAO appointment;
}
