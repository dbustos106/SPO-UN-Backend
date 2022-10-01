package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.mappers.AppointmentMapper;
import com.app.spoun.mappers.AppointmentMapperImpl;
import com.app.spoun.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class AppointmentService {

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    @Autowired
    private IRoomRepository iRoomRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    public Map<String,Object> addStudentToAppointment(String username, Integer appointment_id){
        Map<String,Object> answer = new TreeMap<>();

        Student student = iStudentRepository.findByUsername(username).orElse(null);
        Appointment appointment = iAppointmentRepository.findById(appointment_id).orElse(null);

        if(student != null && appointment != null) {
            appointment.getStudents().add(student);
            answer.put("message", "Student added successfully");
        }else{
            answer.put("error", "Not successful");
        }

        return answer;

    }

    public Map<String,Object> getAllAppointment (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findAll(page);

        List<AppointmentDTO> listAppointmentsDTO = new ArrayList<>();
        for(Appointment appointment : appointments){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            listAppointmentsDTO.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentsDTO = new PageImpl<>(listAppointmentsDTO);

        if(appointmentsDTO.getSize() != 0){
            answer.put("message", appointmentsDTO);
        }else {
            answer.put("error", "None appointment found");
        }
        return answer;
    }

    public Map<String,Object> findAppointmentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
        if(appointmentDTO != null){
            answer.put("message", appointmentDTO);
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

    public Map<String,Object> saveAppointment(AppointmentDTO appointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointmentDTO != null){
            Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            Patient patient = iPatientRepository.findById(appointmentDTO.getPatient_id()).orElse(null);
            Professor professor = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);
            Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);
            appointment.setRoom(room);
            appointment.setPatient(patient);
            appointment.setProfessor(professor);
            iAppointmentRepository.save(appointment);
            answer.put("message", "Appointment saved successfully");
        }else{
            answer.put("error", "Appointment not saved");
        }
        return answer;
    }

    public Map<String,Object> editAppointment(AppointmentDTO appointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointmentDTO.getId() != null && iAppointmentRepository.existsById(appointmentDTO.getId())){
            Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            Patient patient = iPatientRepository.findById(appointmentDTO.getPatient_id()).orElse(null);
            Professor professor = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);
            Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);
            appointment.setRoom(room);
            appointment.setPatient(patient);
            appointment.setProfessor(professor);
            iAppointmentRepository.save(appointment);
            answer.put("message", "Appointment updated successfully");
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAppointment(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAppointmentRepository.existsById(id)){
            iAppointmentRepository.deleteById(id);
            answer.put("message", "Appointment deleted successfully");
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

}