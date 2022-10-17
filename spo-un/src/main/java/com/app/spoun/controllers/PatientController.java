package com.app.spoun.controllers;

import com.app.spoun.dto.PatientDTO;
import com.app.spoun.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/patient")
public class PatientController {

    @Autowired
    private PatientService patientService;


    @PutMapping(value = "/cancelAppointment")
    public ResponseEntity<?> cancelAppointmentById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = patientService.cancelAppointmentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllPatient(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = patientService.getAllPatient(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findPatientById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = patientService.findPatientById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getPatientScheduleByPatientId(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = patientService.getPatientScheduleByPatientId(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editPatient(@RequestBody PatientDTO patientDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = patientService.editPatient(patientDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deletePatient(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = patientService.deletePatient(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}