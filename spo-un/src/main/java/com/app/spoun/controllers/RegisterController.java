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
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

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
    public ResponseEntity<?> saveStudent(@RequestBody StudentDTO studentDTO, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            answer = studentService.saveStudent(studentDTO, siteUrl);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/verifyAccount/student")
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
    public ResponseEntity<?> saveProfessor(@RequestBody ProfessorDTO professorDTO, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            answer = professorService.saveProfessor(professorDTO, siteUrl);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/verifyAccount/professor")
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
    public ResponseEntity<?> savePatient(@RequestBody PatientDTO patientDTO, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            answer = patientService.savePatient(patientDTO, siteUrl);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/verifyAccount/patient")
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
    public ResponseEntity<?> saveAdmin(@RequestBody AdminDTO adminDTO, HttpServletRequest request)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();
        try{
            String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
            answer = adminService.saveAdmin(adminDTO, siteUrl);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/verifyAccount/admin")
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
