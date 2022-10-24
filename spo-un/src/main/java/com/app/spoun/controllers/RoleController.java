package com.app.spoun.controllers;

import com.app.spoun.dto.RoleDTO;
import com.app.spoun.services.RoleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {

    private RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService){
        this.roleService = roleService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRole(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = roleService.getAllRole(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = roleService.findRoleById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveRole(@RequestBody RoleDTO roleDTO){
        try{
            Map<String, Object> answer = roleService.saveRole(roleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editRole(@RequestBody RoleDTO roleDTO){
        try{
            Map<String, Object> answer = roleService.editRole(roleDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = roleService.deleteRole(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}
