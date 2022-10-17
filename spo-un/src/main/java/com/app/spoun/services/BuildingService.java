package com.app.spoun.services;

import com.app.spoun.domain.Building;
import com.app.spoun.dto.BuildingDTO;
import com.app.spoun.mappers.BuildingMapper;
import com.app.spoun.mappers.BuildingMapperImpl;
import com.app.spoun.repository.IBuildingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class BuildingService{

    @Autowired
    private IBuildingRepository iBuildingRepository;

    private BuildingMapper buildingMapper = new BuildingMapperImpl();


    public Map<String,Object> getAllBuilding(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of buildings
        Pageable page = PageRequest.of(idPage, size);
        Page<Building> buildings = iBuildingRepository.findAll(page);

        // map all buildings
        List<BuildingDTO> listBuildingDTOS = new ArrayList<>();
        for(Building building : buildings){
            BuildingDTO buildingDTO = buildingMapper.buildingToBuildingDTO(building);
            listBuildingDTOS.add(buildingDTO);
        }
        Page<BuildingDTO> buildingDTOS = new PageImpl<>(listBuildingDTOS);

        // return page of buildings
        if(buildingDTOS.getSize() != 0){
            answer.put("message", buildingDTOS);
        }else {
            answer.put("error", "No building found");
        }
        return answer;
    }

    public Map<String,Object> findBuildingById(Long id){
        Map<String,Object> answer = new TreeMap<>();
        Building building = iBuildingRepository.findById(id).orElse(null);
        BuildingDTO buildingDTO = buildingMapper.buildingToBuildingDTO(building);
        if(buildingDTO != null){
            answer.put("message", buildingDTO);
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> saveBuilding(BuildingDTO buildingDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(buildingDTO != null){
            // save building
            Building building = buildingMapper.buildingDTOToBuilding(buildingDTO);
            building.setRooms(new ArrayList<>());
            iBuildingRepository.save(building);
            answer.put("message", "Building saved successfully");
        }else{
            answer.put("error", "Building not saved");
        }
        return answer;
    }

    public Map<String,Object> editBuilding(BuildingDTO buildingDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(buildingDTO.getId() != null && iBuildingRepository.existsById(buildingDTO.getId())){
            // update building
            Building building = buildingMapper.buildingDTOToBuilding(buildingDTO);
            iBuildingRepository.save(building);
            answer.put("message", "Building updated successfully");
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> deleteBuilding(Long id){
        Map<String,Object> answer = new TreeMap<>();
        if(iBuildingRepository.existsById(id)){
            iBuildingRepository.deleteById(id);
            answer.put("message", "Building deleted successfully");
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

}