package com.app.spoun.services;

import com.app.spoun.repository.IBuildingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Building;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class BuildingService {
    @Autowired
    private IBuildingRepository iBuildingRepository;

    public Map<String,Object> getAllBuilding (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Building> buildings = iBuildingRepository.findAll(page);
        if(buildings.getSize() != 0){
            answer.put("buildings", buildings);
        }else {
            answer.put("error", "No building found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Building building = iBuildingRepository.findById(id).orElse(null);
        if(building != null){
            answer.put("building", building);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> saveBuilding(Building building){
        Map<String,Object> answer = new TreeMap<>();
        if(building != null){
            System.out.println("Guardar building");
            Building building_answer = iBuildingRepository.save(building);
            answer.put("building", building_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editBuilding(Building building){
        Map<String,Object> answer = new TreeMap<>();
        if(building.getId() != null && iBuildingRepository.existsById(building.getId())){
            Building building_answer = iBuildingRepository.save(building);
            answer.put("building", building_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Building not found");
        }
        return answer;
    }

    public Map<String,Object> deleteBuilding(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iBuildingRepository.existsById(id)){
            iBuildingRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Building not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iBuildingRepository.existsById(id);
    }

}