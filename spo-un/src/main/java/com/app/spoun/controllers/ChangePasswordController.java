package com.app.spoun.controllers;

import com.app.spoun.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/changePassword")
public class ChangePasswordController {

    private final AdminService adminService;
    private final PatientService patientService;
    private final ProfessorService professorService;
    private final StudentService studentService;
    private final ChangePasswordService changePasswordService;

    @Autowired
    public ChangePasswordController(AdminService adminService,
                                    PatientService patientService,
                                    ProfessorService professorService,
                                    StudentService studentService,
                                    ChangePasswordService changePasswordService){
        this.adminService = adminService;
        this.patientService = patientService;
        this.professorService = professorService;
        this.studentService = studentService;
        this.changePasswordService = changePasswordService;
    }


    @GetMapping(value = "/email")
    public ResponseEntity<?> emailToChangePassword(
            @Param("email") String email){
        try{
            Map<String, Object> answer = changePasswordService.emailToChangePassword(email);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/admin")
    public ResponseEntity<?> changeAdminPassword(
            @Param("code") String code,
            @RequestParam String password){
        try{
            Map<String, Object> answer = adminService.changePassword(code, password);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/patient")
    public ResponseEntity<?> changePatientPassword(
            @Param("code") String code,
            @RequestParam String password){
        try{
            Map<String, Object> answer = patientService.changePassword(code, password);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/professor")
    public ResponseEntity<?> changeProfessorPassword(
            @Param("code") String code,
            @RequestParam String password){
        try{
            Map<String, Object> answer = professorService.changePassword(code, password);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/student")
    public ResponseEntity<?> changeStudentPassword(
            @Param("code") String code,
            @RequestParam String password){
        try{
            Map<String, Object> answer = studentService.changePassword(code, password);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}
