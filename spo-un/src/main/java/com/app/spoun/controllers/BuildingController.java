package com.app.spoun.controllers;

import com.app.spoun.dto.BuildingDTO;
import com.app.spoun.services.BuildingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/building")
public class BuildingController {

    private BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService){
        this.buildingService = buildingService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllBuilding(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = buildingService.getAllBuilding(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findBuildingById(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = buildingService.findBuildingById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveBuilding(@RequestBody BuildingDTO buildingDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = buildingService.saveBuilding(buildingDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editBuilding(@RequestBody BuildingDTO buildingDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = buildingService.editBuilding(buildingDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteBuilding(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = buildingService.deleteBuilding(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}