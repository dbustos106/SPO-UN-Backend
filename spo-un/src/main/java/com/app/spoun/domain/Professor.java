package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "professor", schema = "spo-un")
public class Professor {

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

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @OneToMany(mappedBy = "professor")
    private List<Student> students;

    @OneToMany(mappedBy = "professor")
    private List<Appointment> appointments;

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", document_type='" + document_type + '\'' +
                ", document_number='" + document_number + '\'' +
                ", role=" + role +
                '}';
    }

}