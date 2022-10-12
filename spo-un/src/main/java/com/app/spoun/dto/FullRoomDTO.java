package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FullRoomDTO {

    private RoomDTO roomDTO;

    private BuildingDTO buildingDTO;

}
