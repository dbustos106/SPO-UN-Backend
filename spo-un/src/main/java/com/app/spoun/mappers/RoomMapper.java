package com.app.spoun.mappers;

import com.app.spoun.dao.RoomDAO;
import com.app.spoun.dto.RoomDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface RoomMapper {

    @Mapping(source = "building.id", target = "building_id")
    public RoomDTO roomDAOToRoomDTO(RoomDAO roomDAO);

    @Mapping(source = "building_id", target = "building.id")
    public RoomDAO roomDTOToRoomDAO(RoomDTO roomDTO);

}
