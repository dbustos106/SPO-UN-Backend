package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.CascadeType;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "student", schema = "spo-un")
public class Student {

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
    private Professor professor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_appointment",
            joinColumns = { @JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "appointment_id")})
    private List<Appointment> appointments;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_student",
                joinColumns = { @JoinColumn(name = "student_id")},
                inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

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