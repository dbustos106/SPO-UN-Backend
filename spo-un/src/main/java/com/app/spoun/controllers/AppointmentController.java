package com.app.spoun.controllers;

import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.services.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/appointment")
public class AppointmentController{

    @Autowired
    private AppointmentService appointmentService;

    @PutMapping(value = "/confirm/{appointmentId}/{patientId}")
    public ResponseEntity<?> confirmAppointmentById(
            @PathVariable("appointmentId") Integer appointmentId,
            @PathVariable("patientId") Integer patientId,
            @RequestBody TentativeScheduleDTO tentativeScheduleDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.confirmAppointmentById(appointmentId, patientId, tentativeScheduleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAppointment(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.getAllAppointment(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.findAppointmentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.saveAppointment(fullAppointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.editAppointment(fullAppointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.deleteAppointment(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}