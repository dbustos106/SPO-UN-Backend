package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "schedule", schema = "spo-un")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "start_time")
    private String start_time;

    @Column(name = "end_time")
    private String end_time;

    @JoinColumn(name = "appointment_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Appointment appointment;

    @Override
    public String toString() {
        return "Schedule{" +
                "id=" + id +
                ", start_time='" + start_time + '\'' +
                ", end_time='" + end_time + '\'' +
                ", appointment=" + appointment +
                '}';
    }

}
