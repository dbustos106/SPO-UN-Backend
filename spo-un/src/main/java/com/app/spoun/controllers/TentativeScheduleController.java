package com.app.spoun.controllers;

import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.services.TentativeScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/tentativeSchedule")
public class TentativeScheduleController {

    @Autowired
    private TentativeScheduleService tentativeScheduleService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllTentativeSchedule(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = tentativeScheduleService.getAllTentativeSchedule(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findTentativeScheduleById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = tentativeScheduleService.findTentativeScheduleById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveTentativeSchedule(@RequestBody TentativeScheduleDTO tentativeScheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editTentativeSchedule(@RequestBody TentativeScheduleDTO tentativeScheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = tentativeScheduleService.editTentativeSchedule(tentativeScheduleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteTentativeSchedule(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = tentativeScheduleService.deleteTentativeSchedule(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}