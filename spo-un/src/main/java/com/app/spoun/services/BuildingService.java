package com.app.spoun.services;

import com.app.spoun.domain.Building;
import com.app.spoun.dto.BuildingDTO;
import com.app.spoun.mappers.BuildingMapper;
import com.app.spoun.repository.IBuildingRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.*;

@Transactional
@Service
public class BuildingService{

    private final IBuildingRepository iBuildingRepository;
    private final BuildingMapper buildingMapper;

    @Autowired
    public BuildingService(IBuildingRepository iBuildingRepository,
                           BuildingMapper buildingMapper){
        this.iBuildingRepository = iBuildingRepository;
        this.buildingMapper = buildingMapper;
    }


    public Map<String, Object> getAllBuilding(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

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
        answer.put("message", buildingDTOS);

        return answer;
    }

    public Map<String, Object> findBuildingById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Building building = iBuildingRepository.findById(id).orElse(null);
        if(building != null){
            BuildingDTO buildingDTO = buildingMapper.buildingToBuildingDTO(building);
            answer.put("message", buildingDTO);
        }else{
            throw new NotFoundException("Building not found");
        }
        return answer;
    }

    public Map<String, Object> saveBuilding(BuildingDTO buildingDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(buildingDTO == null){
            throw new IllegalStateException("Request data missing");
        }else{
            // save building
            Building building = buildingMapper.buildingDTOToBuilding(buildingDTO);
            building.setRooms(new ArrayList<>());
            iBuildingRepository.save(building);
            answer.put("message", "Building saved successfully");
        }
        return answer;
    }

    public Map<String, Object> editBuilding(BuildingDTO buildingDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(buildingDTO == null){
            throw new IllegalStateException("Request data missing");
        }else {
            Building building = iBuildingRepository.findById(buildingDTO.getId()).orElse(null);
            if(building == null){
                throw new NotFoundException("Building not found");
            }else{
                // update building
                building.setName(buildingDTO.getName());

                iBuildingRepository.save(building);
                answer.put("message", "Building updated successfully");
            }
        }
        return answer;
    }

    public Map<String, Object> deleteBuilding(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(iBuildingRepository.existsById(id)){
            iBuildingRepository.deleteById(id);
            answer.put("message", "Building deleted successfully");
        }else{
            throw new NotFoundException("Building not found");
        }
        return answer;
    }

}