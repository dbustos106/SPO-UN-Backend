package com.app.spoun.controllers;

import com.app.spoun.dto.RoomDTO;
import com.app.spoun.services.RoomService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/room")
public class RoomController {

    private final RoomService roomService;

    @Autowired
    public RoomController(RoomService roomService){
        this.roomService = roomService;
    }


    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllRoom(
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        try{
            Map<String, Object> answer = roomService.getAllRoom(page, size);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findRoomById(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = roomService.findRoomById(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @GetMapping(value = "/{id}/schedules")
    public ResponseEntity<?> getSchedulesByRoomId(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = roomService.getSchedulesByRoomId(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveRoom(@RequestBody RoomDTO roomDTO){
        try{
            Map<String, Object> answer = roomService.saveRoom(roomDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editRoom(@RequestBody RoomDTO roomDTO){
        try{
            Map<String, Object> answer = roomService.editRoom(roomDTO);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<?> deleteRoom(@PathVariable("id") Long id){
        try{
            Map<String, Object> answer = roomService.deleteRoom(id);
            return ResponseEntity.ok().body(answer);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e);
        }
    }

}