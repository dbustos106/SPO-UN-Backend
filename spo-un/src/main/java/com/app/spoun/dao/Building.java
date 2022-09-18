package com.app.spoun.models;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "building", schema = "spo-un")
public class Building {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

}