package com.app.spoun.models;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "student", schema = "spo-un")
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
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

    /*
    @Column(name = "professor_id")
    private Integer professor_id;
    */
}