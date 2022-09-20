package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "building", schema = "spo-un")
public class BuildingDAO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "building")
    private List<RoomDAO> rooms;

    @Override
    public String toString() {
        return "BuildingDAO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

}