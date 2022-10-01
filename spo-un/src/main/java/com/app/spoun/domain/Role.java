package com.app.spoun.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "Role", schema = "spo-un")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "role")
    private List<Student> students;

    @OneToMany(mappedBy = "role")
    private List<Professor> professors;

    @OneToMany(mappedBy = "role")
    private List<Patient> patients;

    @OneToMany(mappedBy = "role")
    private List<Admin> admins;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
