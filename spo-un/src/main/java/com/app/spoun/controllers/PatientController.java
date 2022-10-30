package com.app.spoun.controllers;

import com.app.spoun.dto.PatientDTO;
import com.app.spoun.services.PatientService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService){
        this.patientService = patientService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllPatient(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = patientService.getAllPatient(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findPatientById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = patientService.findPatientById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getPatientScheduleByPatientId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = patientService.getPatientScheduleByPatientId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/appointments")
    public ResponseEntity<?> getAppointmentsByPatientId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = patientService.getAppointmentsByPatientId(page, size, id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/cancelAppointment/{id}")
    public ResponseEntity<?> cancelAppointmentByAppointmentId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = patientService.cancelAppointmentByAppointmentId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editPatient(@RequestBody PatientDTO patientDTO){
        try{
            Map<String, Object> answer = patientService.editPatient(patientDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deletePatient(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = patientService.deletePatient(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}