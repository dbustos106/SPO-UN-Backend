package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;
/*
import java.io.Serializable;

@Data
@Entity
@Table(name = "student_appointment", schema = "spo-un")
public class Student_AppointmentDAO {

    @EmbeddedId
    private Student_AppointmentDAO_PK student_appointmentDAO_PK;

}

@Embeddable
class Student_AppointmentDAO_PK implements Serializable {

    private static final long serialVersionUID = -2889434213251668915L;
    @JoinColumn(name = "student_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StudentDAO student;

    @JoinColumn(name = "appointment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private AppointmentDAO appointment;
}
 */