package com.app.spoun.mappers;

import com.app.spoun.domain.Room;
import com.app.spoun.dto.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoomMapper {

    @Mapping(source = "building.id", target = "building_id")
    public RoomDTO roomToRoomDTO(Room room);

    @Mapping(source = "building_id", target = "building.id")
    public Room roomDTOToRoom(RoomDTO roomDTO);

}
