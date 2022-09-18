package com.app.spoun.mappers;

import com.app.spoun.dao.BuildingDAO;
import com.app.spoun.dto.BuildingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface BuildingMapper {

    public BuildingDTO buildingDAOToBuildingDTO(BuildingDAO buildingDAO);

    public BuildingDAO buildingDTOToBuildingDAO(BuildingDTO buildingDTO);

}
