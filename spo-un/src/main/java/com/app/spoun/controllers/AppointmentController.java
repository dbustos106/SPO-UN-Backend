package com.app.spoun.controllers;

import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.services.AppointmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController{

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }


    @PutMapping(value = "/{appointmentId}/confirmPatient/{patientId}")
    public ResponseEntity<?> confirmAppointmentByAppointmentId(
            @PathVariable("appointmentId") Long appointmentId,
            @PathVariable("patientId") Long patientId,
            @RequestBody ScheduleDTO scheduleDTO){
        try{
            Map<String, Object> answer = appointmentService.confirmAppointmentByAppointmentId(appointmentId, patientId, scheduleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAppointment(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = appointmentService.getAllAppointment(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/allAvailable")
    public ResponseEntity<?> getAllAvailableAppointment(){
        try{
            Map<String, Object> answer = appointmentService.getAllAvailableAppointment();
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = appointmentService.findAppointmentById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        try{
            Map<String, Object> answer = appointmentService.saveAppointment(fullAppointmentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAppointment(@RequestBody FullAppointmentDTO fullAppointmentDTO){
        try{
            Map<String, Object> answer = appointmentService.editAppointment(fullAppointmentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = appointmentService.deleteAppointment(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}