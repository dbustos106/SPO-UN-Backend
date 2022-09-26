package com.app.spoun.controllers;

import com.app.spoun.dto.RoomDTO;
import com.app.spoun.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/room")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRoom (
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = roomService.getAllRoom(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findRoomById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roomService.findRoomById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveRoom(@RequestBody RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roomService.saveRoom(roomDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editRoom(@RequestBody RoomDTO roomDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roomService.editRoom(roomDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roomService.deleteRoom(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}