package com.app.spoun.dao;

import javax.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "patient", schema = "spo-un")
public class PatientDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "document_type")
    private String document_type;

    @Column(name = "document_number")
    private String document_number;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_type")
    private String blood_type;

    @OneToMany(mappedBy = "patient")
    private List<AntecedentDAO> antecedents;

    @OneToMany(mappedBy = "patient")
    private List<AppointmentDAO> appointments;

}