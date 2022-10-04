package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.Appointment_ScheduleDTO;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.mappers.AppointmentMapper;
import com.app.spoun.mappers.AppointmentMapperImpl;
import com.app.spoun.mappers.ScheduleMapper;
import com.app.spoun.mappers.ScheduleMapperImpl;
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

    @Autowired
    private IScheduleRepository iScheduleRepository;

    @Autowired
    private ScheduleService scheduleService;

    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    private ScheduleMapper scheduleMapper = new ScheduleMapperImpl();

    public Map<String,Object> addStudentToAppointment(String username, Integer appointment_id){
        Map<String,Object> answer = new TreeMap<>();

        // get student and appointment
        Student student = iStudentRepository.findByUsername(username).orElse(null);
        Appointment appointment = iAppointmentRepository.findById(appointment_id).orElse(null);

        // add student to appointment
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

        // get page of appointments
        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findAll(page);

        // map all appointments
        List<AppointmentDTO> listAppointmentsDTO = new ArrayList<>();
        for(Appointment appointment : appointments){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            listAppointmentsDTO.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentsDTO = new PageImpl<>(listAppointmentsDTO);

        // return page of appointments
        if(appointmentsDTO.getSize() != 0){
            answer.put("message", appointmentsDTO);
        }else {
            answer.put("error", "No appointment found");
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

    public Map<String,Object> saveAppointment(Appointment_ScheduleDTO appointment_scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointment_scheduleDTO != null){

            // extract objects in Appointment_scheduleDTO
            AppointmentDTO appointmentDTO = appointment_scheduleDTO.getAppointment();
            List<ScheduleDTO> schedulesDTO = appointment_scheduleDTO.getSchedules();
            List<String> students = appointment_scheduleDTO.getStudents();

            // get room and professor
            Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            Professor professor = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);

            // save appointment
            Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);
            appointment.setRoom(room);
            appointment.setPatient(null);
            appointment.setState("Activo");
            appointment.setProfessor(professor);
            appointment.setStudents(new ArrayList<>());
            appointment.setSchedules(new ArrayList<>());
            Appointment appointment_answer = iAppointmentRepository.save(appointment);

            // save schedule
            for (ScheduleDTO scheduleDTO : schedulesDTO) {
                scheduleDTO.setAppointment_id(appointment_answer.getId());
                scheduleService.saveSchedule(scheduleDTO);
            }

            // save students
            for (String student : students) {
                this.addStudentToAppointment(student, appointment_answer.getId());
            }

            answer.put("message", "Appointment saved successfully");
        }else{
            answer.put("error", "Appointment not saved");
        }
        return answer;
    }

    public Map<String,Object> editAppointment(Appointment_ScheduleDTO appointment_scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(appointment_scheduleDTO != null){

            // extract objects in Appointment_scheduleDTO
            AppointmentDTO appointmentDTO = appointment_scheduleDTO.getAppointment();
            List<ScheduleDTO> schedulesDTO = appointment_scheduleDTO.getSchedules();
            List<String> students = appointment_scheduleDTO.getStudents();

            if(appointmentDTO.getId() != null && iAppointmentRepository.existsById(appointmentDTO.getId())) {

                // get appointment
                Appointment appointment = iAppointmentRepository.findById(appointmentDTO.getId()).orElse(null);

                // get room and professor
                Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
                Professor professor = iProfessorRepository.findById(appointmentDTO.getProfessor_id()).orElse(null);

                // if patient is null, update procedure type
                if(appointment.getPatient() == null) {
                    appointment.setProcedure_type(appointmentDTO.getProcedure_type());
                }

                // update appointment
                appointment.setRoom(room);
                appointment.setProfessor(professor);
                appointment.setStudents(new ArrayList<>());
                appointment.setSchedules(new ArrayList<>());

                // delete schedules
                iScheduleRepository.deleteByAppointment_id(appointment.getId());

                // update schedule
                for (ScheduleDTO scheduleDTO : schedulesDTO) {
                    scheduleDTO.setAppointment_id(appointment.getId());
                    scheduleService.saveSchedule(scheduleDTO);
                }

                // update students
                for (String student : students) {
                    this.addStudentToAppointment(student, appointment.getId());
                }

                iAppointmentRepository.save(appointment);
                answer.put("message", "Appointment updated successfully");
            }else{
                answer.put("error", "Appointment not found");
            }
        }else{
            answer.put("error", "Appointment not edited");
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