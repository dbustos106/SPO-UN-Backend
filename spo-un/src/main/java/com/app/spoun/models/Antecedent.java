package com.app.spoun.models;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "antecedent", schema = "spo-un")
public class Antecedent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    private Integer id;

    @Column(name = "type")
    private String type;

    @Column(name = "description")
    private String description;

    @JoinColumn(name = "patient_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Patient patient;
}