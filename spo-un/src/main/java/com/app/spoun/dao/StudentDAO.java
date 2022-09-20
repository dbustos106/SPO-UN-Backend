package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
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

    @JoinColumn(name = "Professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ProfessorDAO professor;

    @OneToMany(mappedBy = "student")
    private List<Student_AppointmentDAO> appointments;

    @Override
    public String toString() {
        return "StudentDAO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", document_type='" + document_type + '\'' +
                ", document_number='" + document_number + '\'' +
                ", professor=" + professor +
                '}';
    }

}