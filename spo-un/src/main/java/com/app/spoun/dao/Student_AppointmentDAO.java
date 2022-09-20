package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(value = Student_AppointmentDAO_PK.class)
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

    @Override
    public String toString() {
        return "Student_AppointmentDAO{" +
                "student=" + student +
                ", appointment=" + appointment +
                '}';
    }

}
