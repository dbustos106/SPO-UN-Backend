package com.app.spoun.models;

import javax.persistence.*;

import lombok.Data;

@Data
@Entity
@Table(name = "room", schema = "spo-un")
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;


    /*
    @Column(name = "building_id")
    private Integer building_id;
    */
}