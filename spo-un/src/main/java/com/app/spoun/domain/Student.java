package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "student", schema = "spo-un")
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String last_name;

    @Column(name = "email")
    private String email;

    @Column(name = "document_type")
    private String document_type;

    @Column(name = "document_number")
    private String document_number;

    @Column(name = "verification_code")
    private String verification_code;

    @Column(name = "enabled")
    private boolean enabled;

    @JoinColumn(name = "Professor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Professor professor;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "student_appointment",
            joinColumns = {@JoinColumn(name = "student_id")},
            inverseJoinColumns = {@JoinColumn(name = "appointment_id")})
    private List<Appointment> appointments;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", document_type='" + document_type + '\'' +
                ", document_number='" + document_number + '\'' +
                ", verification_code='" + verification_code + '\'' +
                ", enabled='" + enabled + '\'' +
                ", professor=" + professor +
                ", role=" + role +
                '}';
    }

}