package com.app.spoun.models;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "admin", schema = "spo-un")
public class Admin {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}