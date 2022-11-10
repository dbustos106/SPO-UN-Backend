package com.app.spoun.controllers;

import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.services.TentativeScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tentativeSchedule")
public class TentativeScheduleController {

    private final TentativeScheduleService tentativeScheduleService;

    @Autowired
    public TentativeScheduleController(TentativeScheduleService tentativeScheduleService){
        this.tentativeScheduleService = tentativeScheduleService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTentativeSchedule(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = tentativeScheduleService.getAllTentativeSchedule(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findTentativeScheduleById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = tentativeScheduleService.findTentativeScheduleById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveTentativeSchedule(@RequestBody TentativeScheduleDTO tentativeScheduleDTO){
        try{
            Map<String, Object> answer = tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editTentativeSchedule(@RequestBody TentativeScheduleDTO tentativeScheduleDTO){
        try{
            Map<String, Object> answer = tentativeScheduleService.editTentativeSchedule(tentativeScheduleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteTentativeSchedule(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = tentativeScheduleService.deleteTentativeSchedule(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}