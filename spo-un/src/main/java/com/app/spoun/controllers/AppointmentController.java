package com.app.spoun.controllers;

import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.services.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/appointment")
public class AppointmentController{

    private AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }


    @PutMapping(value = "/{appointmentId}/confirmPatient/{patientId}")
    public ResponseEntity<?> confirmAppointmentByAppointmentId(
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("patientId") Long patientId,
            @RequestBody ScheduleDTO scheduleDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.confirmAppointmentByAppointmentId(appointmentId, patientId, scheduleDTO);
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

    @GetMapping(value = "/allAvailable")
    public ResponseEntity<?> getAllAvailableAppointment(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.getAllAvailableAppointment(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.findAppointmentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.saveAppointment(fullAppointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.editAppointment(fullAppointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.deleteAppointment(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}