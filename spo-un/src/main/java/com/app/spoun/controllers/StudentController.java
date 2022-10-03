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

    @GetMapping(value = "/confirmedSchedule/{id}")
    public ResponseEntity<?> getStudentConfirmedScheduleById(@PathVariable("id") Integer id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getStudentConfirmedScheduleById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/schedule/{id}")
    public ResponseEntity<?> getStudentScheduleById(@PathVariable("id") Integer id){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.getStudentScheduleById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllStudent (
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
    public ResponseEntity<?> findStudentById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.findStudentById(id);
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

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = studentService.deleteStudent(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}