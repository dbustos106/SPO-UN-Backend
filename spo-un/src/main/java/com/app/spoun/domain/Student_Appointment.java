package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(value = Student_Appointment_PK.class)
@Table(name = "student_appointment", schema = "spo-un")
public class Student_Appointment {

    @Id
    @JoinColumn(name = "student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Student student;

    @Id
    @JoinColumn(name = "appointment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Appointment appointment;

    @Override
    public String toString() {
        return "Student_AppointmentDAO{" +
                "student=" + student +
                ", appointment=" + appointment +
                '}';
    }

}
