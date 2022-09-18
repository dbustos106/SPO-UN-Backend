package com.app.spoun.dao;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "room", schema = "spo-un")
public class RoomDAO {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Integer id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "building_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private BuildingDAO building;

    @OneToMany(mappedBy = "room")
    private List<AppointmentDAO> appointments;

}