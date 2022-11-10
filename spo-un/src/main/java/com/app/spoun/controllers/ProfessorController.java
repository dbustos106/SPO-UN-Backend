package com.app.spoun.controllers;

import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.services.ProfessorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/professor")
public class ProfessorController {

    private final ProfessorService professorService;

    @Autowired
    public ProfessorController(ProfessorService professorService){
        this.professorService = professorService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllProfessor(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = professorService.getAllProfessor(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findProfessorById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = professorService.findProfessorById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getProfessorScheduleByProfessorId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = professorService.getProfessorScheduleByProfessorId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/students")
    public ResponseEntity<?> getStudentsByProfessorId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = professorService.getStudentsByProfessorId(page, size, id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/appointments")
    public ResponseEntity<?> getAppointmentsByProfessorId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = professorService.getAppointmentsByProfessorId(page, size, id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editProfessor(@RequestBody ProfessorDTO professorDTO){
        try{
            Map<String, Object> answer = professorService.editProfessor(professorDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteProfessor(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = professorService.deleteProfessor(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}
