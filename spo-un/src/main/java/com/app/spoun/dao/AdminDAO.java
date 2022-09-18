package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "admin", schema = "spo-un")
public class AdminDAO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

}