package com.app.spoun.controllers;

import com.app.spoun.dto.BuildingDTO;
import com.app.spoun.services.BuildingService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        try{
            Map<String, Object> answer = buildingService.getAllBuilding(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findBuildingById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = buildingService.findBuildingById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveBuilding(@RequestBody BuildingDTO buildingDTO){
        try{
            Map<String, Object> answer = buildingService.saveBuilding(buildingDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editBuilding(@RequestBody BuildingDTO buildingDTO){
        try{
            Map<String, Object> answer = buildingService.editBuilding(buildingDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteBuilding(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = buildingService.deleteBuilding(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}