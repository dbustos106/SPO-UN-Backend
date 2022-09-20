package com.app.spoun.mappers;

import com.app.spoun.domain.Building;
import com.app.spoun.dto.BuildingDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BuildingMapper {

    public BuildingDTO buildingToBuildingDTO(Building building);

    public Building buildingDTOToBuilding(BuildingDTO buildingDTO);

}
