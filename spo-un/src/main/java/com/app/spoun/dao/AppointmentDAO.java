package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "appointment", schema = "spo-un")
public class AppointmentDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "start_time")
    private String start_time;

    @Column(name = "end_time")
    private String end_time;

    @Column(name = "procedure_type")
    private String procedure_type;

    @Column(name = "state")
    private String state;

    @Column(name = "cancel_reason")
    private String cancel_reason;

    @Column(name = "patient_rating")
    private Integer patient_rating;

    @Column(name = "patient_feedback")
    private String patient_feedback;

    @JoinColumn(name = "room_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RoomDAO room;

    @JoinColumn(name = "patient_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PatientDAO patient;

    @JoinColumn(name = "professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfessorDAO professor;

    @OneToMany(mappedBy = "appointment")
    private List<Student_AppointmentDAO> students;

}