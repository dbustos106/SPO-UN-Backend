package com.app.spoun.controllers;

import com.app.spoun.dto.AdminDTO;
import com.app.spoun.services.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService){
        this.adminService = adminService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAdmin(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = adminService.getAllAdmin(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAdminById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = adminService.findAdminById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAdmin(@RequestBody AdminDTO adminDTO){
        try{
            Map<String, Object> answer = adminService.editAdmin(adminDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteAdmin(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = adminService.deleteAdmin(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}