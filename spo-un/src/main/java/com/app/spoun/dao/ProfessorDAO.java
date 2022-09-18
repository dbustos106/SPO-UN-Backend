package com.app.spoun.dao;

import javax.persistence.*;
import java.util.List;
import lombok.Data;

@Data
@Entity
@Table(name = "professor", schema = "spo-un")
public class ProfessorDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
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

    @OneToMany(mappedBy = "professor")
    private List<StudentDAO> students;

    @OneToMany(mappedBy = "professor")
    private List<AppointmentDAO> appointments;

}