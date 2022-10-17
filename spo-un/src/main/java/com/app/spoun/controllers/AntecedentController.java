package com.app.spoun.controllers;

import com.app.spoun.dto.AntecedentDTO;
import com.app.spoun.services.AntecedentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/antecedent")
public class AntecedentController {

    @Autowired
    private AntecedentService antecedentService;


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAntecedent(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.getAllAntecedent(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAntecedentById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.findAntecedentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAntecedent(@RequestBody AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.saveAntecedent(antecedentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAntecedent(@RequestBody AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.editAntecedent(antecedentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAntecedent(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.deleteAntecedent(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}