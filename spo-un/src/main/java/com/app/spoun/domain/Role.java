package com.app.spoun.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.CascadeType;

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

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_student",
            joinColumns = { @JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "student_id")})
    private List<Student> students;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_professor",
            joinColumns = { @JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "professor_id")})
    private List<Student> professors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_patient",
            joinColumns = { @JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "patient_id")})
    private List<Student> patients;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "role_admin",
            joinColumns = { @JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "admin_id")})
    private List<Student> admins;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}
