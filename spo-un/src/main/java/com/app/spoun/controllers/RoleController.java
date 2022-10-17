package com.app.spoun.controllers;

import com.app.spoun.dto.RoleDTO;
import com.app.spoun.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRole(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = roleService.getAllRole(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.findRoleById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveRole(@RequestBody RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.saveRole(roleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editRole(@RequestBody RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.editRole(roleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.deleteRole(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}
