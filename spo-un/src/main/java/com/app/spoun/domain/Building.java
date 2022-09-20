package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "building", schema = "spo-un")
public class Building {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "building")
    private List<Room> rooms;

    @Override
    public String toString() {
        return "BuildingDAO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}