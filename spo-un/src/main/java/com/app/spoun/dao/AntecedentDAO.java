package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "antecedent", schema = "spo-un")
public class AntecedentDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "patient_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private PatientDAO patient;

}