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

@RestController
@RequestMapping("/register")
public class RegisterController {

    private StudentService studentService;
    private ProfessorService professorService;
    private PatientService patientService;
    private AdminService adminService;

    @Autowired
    public RegisterController(StudentService studentService,
                              ProfessorService professorService,
                              PatientService patientService,
                              AdminService adminService){
        this.studentService = studentService;
        this.professorService = professorService;
        this.patientService = patientService;
        this.adminService = adminService;
    }


    @PostMapping(value = "/student")
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO)
            throws UnsupportedEncodingException, MessagingException {
        try{
            Map<String, Object> answer = studentService.saveStudent(studentDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/verifyAccount/student")
    public ResponseEntity<?> verifyStudent(@Param("code") String code){
        try{
            Map<String, Object> answer = studentService.verifyStudent(code);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/professor")
    public ResponseEntity<?> saveProfessor(@RequestBody ProfessorDTO professorDTO)
            throws UnsupportedEncodingException, MessagingException {
        try{
            Map<String, Object> answer = professorService.saveProfessor(professorDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/verifyAccount/professor")
    public ResponseEntity<?> verifyProfessor(@Param("code") String code){
        try{
            Map<String, Object> answer = professorService.verifyProfessor(code);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/patient")
    public ResponseEntity<?> savePatient(@RequestBody PatientDTO patientDTO)
            throws UnsupportedEncodingException, MessagingException {
        try{
            Map<String, Object> answer = patientService.savePatient(patientDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/verifyAccount/patient")
    public ResponseEntity<?> verifyPatient(@Param("code") String code){
        try{
            Map<String, Object> answer = patientService.verifyPatient(code);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/admin")
    public ResponseEntity<?> saveAdmin(@RequestBody AdminDTO adminDTO)
            throws UnsupportedEncodingException, MessagingException {
        try{
            Map<String, Object> answer = adminService.saveAdmin(adminDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/verifyAccount/admin")
    public ResponseEntity<?> verifyAdmin(@Param("code") String code){
        try{
            Map<String, Object> answer = adminService.verifyAdmin(code);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}
