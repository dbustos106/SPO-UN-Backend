package com.app.spoun.controllers;

import com.app.spoun.dto.AntecedentDTO;
import com.app.spoun.services.AntecedentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/antecedent")
public class AntecedentController {

    private AntecedentService antecedentService;

    @Autowired
    public AntecedentController(AntecedentService antecedentService){
        this.antecedentService = antecedentService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAntecedent(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = antecedentService.getAllAntecedent(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAntecedentById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = antecedentService.findAntecedentById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAntecedent(@RequestBody AntecedentDTO antecedentDTO){
        try{
            Map<String, Object> answer = antecedentService.saveAntecedent(antecedentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAntecedent(@RequestBody AntecedentDTO antecedentDTO){
        try{
            Map<String, Object> answer = antecedentService.editAntecedent(antecedentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAntecedent(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = antecedentService.deleteAntecedent(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}