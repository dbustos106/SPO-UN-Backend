package com.app.spoun.controllers;

import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.Appointment_ScheduleDTO;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.services.AppointmentService;
import com.app.spoun.services.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@RequestMapping("/appointment")
public class AppointmentController{

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping(value = "/addStudent")
    public ResponseEntity<?> addStudentToAppointment(
            @RequestParam String username,
            @RequestParam Integer appointment_id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.addStudentToAppointment(username, appointment_id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/all")
    public ResponseEntity<?> getAllAppointment (
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.getAllAppointment(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findAppointmentById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.findAppointmentById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/save")
    public ResponseEntity<?> saveAppointment(@RequestBody Appointment_ScheduleDTO appointment_scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            AppointmentDTO appointmentDTO = appointment_scheduleDTO.getAppointment();
            List<ScheduleDTO> schedulesDTO = appointment_scheduleDTO.getSchedules();
            List<String> students = appointment_scheduleDTO.getStudents();

            // Save appointment
            answer = appointmentService.saveAppointment(appointmentDTO);

            // Save schedule
            for(ScheduleDTO scheduleDTO : schedulesDTO){
                scheduleDTO.setAppointment_id((Integer) answer.get("id"));
                scheduleService.saveSchedule(scheduleDTO);
            }

            // Save students
            for(String student : students){
                appointmentService.addStudentToAppointment(student, (Integer) answer.get("id"));
            }
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer.get("message"));
    }

    @PutMapping(value = "/edit")
    public ResponseEntity<?> editAppointment(@RequestBody AppointmentDTO appointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.editAppointment(appointmentDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deleteAppointment(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = appointmentService.deleteAppointment(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}