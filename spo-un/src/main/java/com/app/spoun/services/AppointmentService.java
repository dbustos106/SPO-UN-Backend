package com.app.spoun.services;

import com.app.spoun.dao.AppointmentDAO;
import com.app.spoun.dao.PatientDAO;
import com.app.spoun.dao.ProfessorDAO;
import com.app.spoun.dao.RoomDAO;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.mappers.AppointmentMapper;
import com.app.spoun.mappers.AppointmentMapperImpl;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IRoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    public Map<String,Object> getAllAppointment (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<AppointmentDAO> appointmentsDAO = iAppointmentRepository.findAll(page);

        List<AppointmentDTO> listAppointmentsDTO = new ArrayList<>();
        for(AppointmentDAO appointmentDAO: appointmentsDAO){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentDAOToAppointmentDTO(appointmentDAO);
            listAppointmentsDTO.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentsDTO = new PageImpl<>(listAppointmentsDTO);

        if(appointmentsDTO.getSize() != 0){
            answer.put("appointments", appointmentsDTO);
        }else {
            answer.put("error", "None appointment found");
        }
        return answer;
    }

    public Map<String,Object> findAppointmentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        AppointmentDAO appointmentDAO = iAppointmentRepository.findById(id).orElse(null);
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentDAOToAppointmentDTO(appointmentDAO);
        if(appointmentDTO != null){
            answer.put("appointment", appointmentDTO);
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

    public Map<String,Object> saveAppointment(AppointmentDTO appointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointmentDTO != null){
            RoomDAO roomDAO = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            PatientDAO patientDAO = iPatientRepository.findById(appointmentDTO.getPatient_id()).orElse(null);
            ProfessorDAO professorDAO = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);
            AppointmentDAO appointmentDAO = appointmentMapper.appointmentDTOToAppointmentDAO(appointmentDTO);
            appointmentDAO.setRoom(roomDAO);
            appointmentDAO.setPatient(patientDAO);
            appointmentDAO.setProfessor(professorDAO);
            iAppointmentRepository.save(appointmentDAO);
            answer.put("message", "Appointment saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editAppointment(AppointmentDTO appointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointmentDTO.getId() != null && iAppointmentRepository.existsById(appointmentDTO.getId())){
            RoomDAO roomDAO = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            PatientDAO patientDAO = iPatientRepository.findById(appointmentDTO.getPatient_id()).orElse(null);
            ProfessorDAO professorDAO = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);
            AppointmentDAO appointmentDAO = appointmentMapper.appointmentDTOToAppointmentDAO(appointmentDTO);
            appointmentDAO.setRoom(roomDAO);
            appointmentDAO.setPatient(patientDAO);
            appointmentDAO.setProfessor(professorDAO);
            iAppointmentRepository.save(appointmentDAO);
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
            answer.put("menssage", "Appointment deleted successfully");
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

}