package com.app.spoun.controllers;

import com.app.spoun.models.Admin;
import com.app.spoun.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    public ResponseEntity<?> getAllAdmin (
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = adminService.getAllAdmin(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAdminById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = adminService.findById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping
    public ResponseEntity<?> saveAdmin(@RequestBody Admin admin){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = adminService.saveAdmin(admin);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping
    public ResponseEntity<?> editAdmin(@RequestBody Admin admin){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = adminService.editAdmin(admin);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = adminService.deleteAdmin(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}