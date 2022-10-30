package com.app.spoun.controllers;

import com.app.spoun.dto.StudentDTO;
import com.app.spoun.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/student")
public class StudentController {

    private StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService){
        this.studentService = studentService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStudent(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = studentService.getAllStudent(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findStudentById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = studentService.findStudentById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/schedule")
    public ResponseEntity<?> getStudentScheduleByStudentId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = studentService.getStudentScheduleByStudentId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/unconfirmedSchedule")
    public ResponseEntity<?> getStudentUnconfirmedScheduleByStudentId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = studentService.getStudentUnconfirmedScheduleByStudentId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/appointments")
    public ResponseEntity<?> getAppointmentsByStudentId(
            @PathVariable("id") Long id,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = studentService.getAppointmentsByStudentId(page, size, id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/cancelAppointment/{id}")
    public ResponseEntity<?> cancelAppointmentByAppointmentId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = studentService.cancelAppointmentByAppointmentId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editStudent(@RequestBody StudentDTO studentDTO){
        try{
            Map<String, Object> answer = studentService.editStudent(studentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = studentService.deleteStudent(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}