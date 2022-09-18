package com.app.spoun.models;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "patient", schema = "spo-un")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "document_type")
    private String document_type;

    @Column(name = "document_number")
    private String document_number;

    @Column(name = "age")
    private String age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "blood_type")
    private String blood_type;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Antecedent> antecedents;
}