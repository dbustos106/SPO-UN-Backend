package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "student", schema = "spo-un")
public class StudentDAO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
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

    @JoinColumn(name = "professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfessorDAO professor;

}