package com.app.spoun.controllers;

import com.app.spoun.models.Antecedent;
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

    @GetMapping
    public ResponseEntity<?> getAllAntecedent (
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
    public ResponseEntity<?> findAntecedentById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.findById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping
    public ResponseEntity<?> saveAntecedent(@RequestBody Antecedent antecedent){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.saveAntecedent(antecedent);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping
    public ResponseEntity<?> editAntecedent(@RequestBody Antecedent antecedent){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.editAntecedent(antecedent);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAntecedent(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = antecedentService.deleteAntecedent(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }
}