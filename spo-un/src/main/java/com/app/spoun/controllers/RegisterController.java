package com.app.spoun.controllers;

import com.app.spoun.dto.AdminDTO;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.services.AdminService;
import com.app.spoun.services.PatientService;
import com.app.spoun.services.ProfessorService;
import com.app.spoun.services.StudentService;

import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private ProfessorService professorService;
    @Autowired
    private PatientService patientService;
    @Autowired
    private AdminService adminService;


    @PostMapping(value = "/student")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.saveStudent(studentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/student/verifyAccount")
    public ResponseEntity<?> verifyStudent(@Param("code") String code){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = studentService.verifyStudent(code);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/professor")
    public ResponseEntity<?> saveProfessor(@RequestBody ProfessorDTO professorDTO)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = professorService.saveProfessor(professorDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/professor/verifyAccount")
    public ResponseEntity<?> verifyProfessor(@Param("code") String code){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = professorService.verifyProfessor(code);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/patient")
    public ResponseEntity<?> savePatient(@RequestBody PatientDTO patientDTO)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = patientService.savePatient(patientDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/patient/verifyAccount")
    public ResponseEntity<?> verifyPatient(@Param("code") String code){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = patientService.verifyPatient(code);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<?> saveAdmin(@RequestBody AdminDTO adminDTO)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = adminService.saveAdmin(adminDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/admin/verifyAccount")
    public ResponseEntity<?> verifyAdmin(@Param("code") String code){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = adminService.verifyAdmin(code);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}
