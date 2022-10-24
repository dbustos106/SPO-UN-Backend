package com.app.spoun.controllers;

import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.services.ScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService){
        this.scheduleService = scheduleService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllSchedule(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = scheduleService.getAllSchedule(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findScheduleById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = scheduleService.findScheduleById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveSchedule(@RequestBody ScheduleDTO scheduleDTO){
        try{
            Map<String, Object> answer = scheduleService.saveSchedule(scheduleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editSchedule(@RequestBody ScheduleDTO scheduleDTO){
        try{
            Map<String, Object> answer = scheduleService.editSchedule(scheduleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = scheduleService.deleteSchedule(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}
