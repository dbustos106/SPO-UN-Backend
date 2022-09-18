package com.app.spoun.controllers;

import com.app.spoun.dao.Student_AppointmentPK;
import com.app.spoun.dto.Student_AppointmentDTO;
import com.app.spoun.services.Student_AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/student_appointment")
public class Student_AppointmentController {

    @Autowired
    private Student_AppointmentService student_AppointmentService;

    @GetMapping
    public ResponseEntity<?> getAllStudent_Appointment (
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = student_AppointmentService.getAllStudent_Appointment(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping
    public ResponseEntity<?> saveStudent_Appointment(@RequestBody Student_AppointmentDTO student_AppointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = student_AppointmentService.saveStudent_Appointment(student_AppointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}