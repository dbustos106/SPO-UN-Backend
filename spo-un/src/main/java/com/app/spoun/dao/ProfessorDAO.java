package com.app.spoun.dao;

import javax.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
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

    @Override
    public String toString() {
        return "ProfessorDAO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", document_type='" + document_type + '\'' +
                ", document_number='" + document_number + '\'' +
                '}';
    }
}