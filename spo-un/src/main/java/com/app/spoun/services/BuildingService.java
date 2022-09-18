package com.app.spoun.services;

import com.app.spoun.dao.BuildingDAO;
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

import java.util.*;

@Service
public class BuildingService {
    @Autowired
    private IBuildingRepository iBuildingRepository;

    private BuildingMapper buildingMapper = new BuildingMapperImpl();

    public Map<String,Object> getAllBuilding (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<BuildingDAO> buildingsDAO = iBuildingRepository.findAll(page);

        List<BuildingDTO> listBuildingsDTO = new ArrayList<>();
        for(BuildingDAO buildingDAO: buildingsDAO){
            BuildingDTO buildingDTO = buildingMapper.buildingDAOToBuildingDTO(buildingDAO);
            listBuildingsDTO.add(buildingDTO);
        }
        Page<BuildingDTO> buildingsDTO = new PageImpl<>(listBuildingsDTO);

        if(buildingsDTO.getSize() != 0){
            answer.put("buildings", buildingsDTO);
        }else {
            answer.put("error", "None building found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        BuildingDAO buildingDAO = iBuildingRepository.findById(id).orElse(null);
        BuildingDTO buildingDTO = buildingMapper.buildingDAOToBuildingDTO(buildingDAO);
        if(buildingDTO != null){
            answer.put("building", buildingDTO);
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> saveBuilding(BuildingDTO buildingDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(buildingDTO != null){
            BuildingDAO buildingDAO = buildingMapper.buildingDTOToBuildingDAO(buildingDTO);
            iBuildingRepository.save(buildingDAO);
            answer.put("building", "Building saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editBuilding(BuildingDTO buildingDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(buildingDTO.getId() != null && iBuildingRepository.existsById(buildingDTO.getId())){
            BuildingDAO buildingDAO = buildingMapper.buildingDTOToBuildingDAO(buildingDTO);
            iBuildingRepository.save(buildingDAO);
            answer.put("building", "Building updated successfully");
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> deleteBuilding(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iBuildingRepository.existsById(id)){
            iBuildingRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Building not found");
        }
        return answer;
    }

}