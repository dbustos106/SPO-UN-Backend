package com.app.spoun.domain;

import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "room", schema = "spo-un")
public class Room {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @JoinColumn(name = "building_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Building building;

    @OneToMany(mappedBy = "room")
    private List<Appointment> appointments;

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", building=" + building +
                '}';
    }

}