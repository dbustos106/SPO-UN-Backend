package com.app.spoun.controllers;

import com.app.spoun.dto.StudentDTO;
import com.app.spoun.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;


    @PutMapping(value = "/cancelAppointment")
    public ResponseEntity<?> cancelAppointmentById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.cancelAppointmentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStudent(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getAllStudent(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findStudentById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.findStudentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getStudentScheduleByStudentId(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getStudentScheduleByStudentId(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/unconfirmedSchedule")
    public ResponseEntity<?> getStudentUnconfirmedScheduleByStudentId(@PathVariable("id") Long id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getStudentUnconfirmedScheduleByStudentId(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}/appointments")
    public ResponseEntity<?> getAppointmentsByStudentId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getAppointmentsByStudentId(page, size, id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editStudent(@RequestBody StudentDTO studentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.editStudent(studentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.deleteStudent(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}