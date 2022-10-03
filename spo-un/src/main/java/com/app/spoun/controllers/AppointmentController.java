package com.app.spoun.controllers;

import com.app.spoun.dto.Appointment_ScheduleDTO;
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

    @PostMapping(value = "/addStudent")
    public ResponseEntity<?> addStudentToAppointment(
            @RequestParam String username,
            @RequestParam Integer appointment_id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.addStudentToAppointment(username, appointment_id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAppointment (
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
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment_ScheduleDTO appointment_scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.saveAppointment(appointment_scheduleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAppointment(@RequestBody Appointment_ScheduleDTO appointment_scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.editAppointment(appointment_scheduleDTO);
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