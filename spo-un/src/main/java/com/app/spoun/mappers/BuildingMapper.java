package com.app.spoun.mappers;

import com.app.spoun.domain.Building;
import com.app.spoun.dto.BuildingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BuildingMapper {

    public BuildingDTO buildingToBuildingDTO(Building building);

    @Mapping(target = "rooms", ignore = true)
    public Building buildingDTOToBuilding(BuildingDTO buildingDTO);

}
