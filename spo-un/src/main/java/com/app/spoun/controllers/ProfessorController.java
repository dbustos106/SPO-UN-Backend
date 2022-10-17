package com.app.spoun.controllers;

import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.services.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    @Autowired
    private ProfessorService professorService;


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllProfessor(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = professorService.getAllProfessor(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findProfessorById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = professorService.findProfessorById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getProfessorScheduleByProfessorId(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = professorService.getProfessorScheduleByProfessorId(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/students")
    public ResponseEntity<?> getStudentsByProfessorId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = professorService.getStudentsByProfessorId(page, size, id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editProfessor(@RequestBody ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = professorService.editProfessor(professorDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteProfessor(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = professorService.deleteProfessor(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}
