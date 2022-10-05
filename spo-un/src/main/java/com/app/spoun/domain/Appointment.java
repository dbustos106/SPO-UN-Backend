package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "appointment", schema = "spo-un")
public class Appointment {

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
    private Room room;

    @JoinColumn(name = "patient_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;

    @JoinColumn(name = "professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Professor professor;

    @OneToMany(mappedBy = "appointment")
    private List<TentativeSchedule> tentativeSchedules;

    @ManyToMany
    @JoinTable(name = "student_appointment",
            joinColumns = {@JoinColumn(name = "appointment_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private List<Student> students;

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", procedure_type='" + procedure_type + '\'' +
                ", state='" + state + '\'' +
                ", cancel_reason='" + cancel_reason + '\'' +
                ", patient_rating=" + patient_rating +
                ", patient_feedback='" + patient_feedback + '\'' +
                ", room=" + room +
                ", patient=" + patient +
                ", professor=" + professor +
                '}';
    }

}