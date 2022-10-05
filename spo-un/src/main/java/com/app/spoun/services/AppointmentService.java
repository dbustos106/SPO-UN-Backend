package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.mappers.*;
import com.app.spoun.mappers.TentativeScheduleMapperImpl;
import com.app.spoun.mappers.TentativeScheduleMapper;
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
    private ITentativeScheduleRepository iTentativeScheduleRepository;

    @Autowired
    private TentativeScheduleService tentativeScheduleService;

    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    private TentativeScheduleMapper tentativeScheduleMapper = new TentativeScheduleMapperImpl();

    public Map<String, Object> confirmAppointmentById(Integer appointmentId, Integer patientId, TentativeScheduleDTO tentativeScheduleDTO){
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(appointmentId).orElse(null);

        if(appointment != null){
            // set start_time and end_time
            appointment.setStart_time(tentativeScheduleDTO.getStart_time());
            appointment.setEnd_time(tentativeScheduleDTO.getEnd_time());

            // get patient
            Patient patient = iPatientRepository.findById(patientId).orElse(null);
            appointment.setPatient(patient);

            // save appointment
            iAppointmentRepository.save(appointment);
            answer.put("message", "Appointment schedule changed successfully");
        }else{
            answer.put("error", "Appointment not found");
        }

        return answer;
    }

    public Map<String,Object> getAllAppointment(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of appointments
        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findAll(page);

        // map all appointments
        List<AppointmentDTO> listAppointmentDTOS = new ArrayList<>();
        for(Appointment appointment : appointments){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            listAppointmentDTOS.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentDTOS = new PageImpl<>(listAppointmentDTOS);

        // return page of appointments
        if(appointmentDTOS.getSize() != 0){
            answer.put("message", appointmentDTOS);
        }else {
            answer.put("error", "No appointment found");
        }
        return answer;
    }

    public Map<String,Object> findAppointmentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();

        // create object Appointment_ScheduleDTO
        FullAppointmentDTO fullAppointment_DTO = new FullAppointmentDTO();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);

        if(appointment != null){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            fullAppointment_DTO.setAppointment(appointmentDTO);

            // get schedules
            List<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.findByAppointment_id(id);
            List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
            for(TentativeSchedule tentativeSchedule : tentativeSchedules){
                TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
                listTentativeScheduleDTOS.add(tentativeScheduleDTO);
            }
            fullAppointment_DTO.setTentativeSchedules(listTentativeScheduleDTOS);

            // get students
            List<Student> students = iStudentRepository.findByAppointment_id(id);
            List<String> usernameStudents = new ArrayList<>();
            for(Student student : students){
                usernameStudents.add(student.getUsername());
            }
            fullAppointment_DTO.setStudents(usernameStudents);

            answer.put("message", fullAppointment_DTO);
        }else{
            answer.put("error", "No appointment found");
        }
        return answer;

    }

    public Map<String,Object> saveAppointment(FullAppointmentDTO fullAppointment_DTO){
        Map<String,Object> answer = new TreeMap<>();
        if(fullAppointment_DTO != null){

            // extract objects in Appointment_scheduleDTO
            AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointment();
            List<TentativeScheduleDTO> tentativeScheduleDTOS = fullAppointment_DTO.getTentativeSchedules();
            List<String> students = fullAppointment_DTO.getStudents();

            // get room and professor
            Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
            Student studentMain = iStudentRepository.findByUsername(students.get(0)).orElse(null);
            Professor professor = studentMain.getProfessor();

            // save appointment
            Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);
            appointment.setRoom(room);
            appointment.setPatient(null);
            appointment.setState("Active");
            appointment.setProfessor(professor);
            appointment.setStudents(new ArrayList<>());
            appointment.setTentativeSchedules(new ArrayList<>());
            Appointment appointment_answer = iAppointmentRepository.save(appointment);

            // save tentative schedule
            for (TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS) {
                tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
                tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
            }

            // save students
            for (String studentUsername : students) {
                Student student = iStudentRepository.findByUsername(studentUsername).orElse(null);
                appointment_answer.getStudents().add(student);
            }

            answer.put("message", "Appointment saved successfully");
        }else{
            answer.put("error", "Appointment not saved");
        }
        return answer;
    }

    public Map<String,Object> editAppointment(FullAppointmentDTO fullAppointment_DTO){
        Map<String,Object> answer = new TreeMap<>();
        if(fullAppointment_DTO != null){

            // extract objects in fullAppointmentDTO
            AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointment();
            List<TentativeScheduleDTO> tentativeScheduleDTOS = fullAppointment_DTO.getTentativeSchedules();
            List<String> students = fullAppointment_DTO.getStudents();

            if(appointmentDTO.getId() != null && iAppointmentRepository.existsById(appointmentDTO.getId())) {

                // get appointment
                Appointment appointment = iAppointmentRepository.findById(appointmentDTO.getId()).orElse(null);

                // get room and professor
                Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
                Student studentMain = iStudentRepository.findByUsername(students.get(0)).orElse(null);
                Professor professor = studentMain.getProfessor();

                // if patient is null, update procedure type
                if(appointment.getPatient() == null) {
                    appointment.setProcedure_type(appointmentDTO.getProcedure_type());
                }

                // update appointment
                appointment.setRoom(room);
                appointment.setProfessor(professor);
                appointment.setStudents(new ArrayList<>());
                appointment.setTentativeSchedules(new ArrayList<>());
                Appointment appointment_answer = iAppointmentRepository.save(appointment);

                // delete tentative schedules
                iTentativeScheduleRepository.deleteByAppointment_id(appointment_answer.getId());

                // update tentative schedule
                for (TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS) {
                    tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
                    tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
                }

                // update students
                for (String usernameStudent : students) {
                    Student student = iStudentRepository.findByUsername(usernameStudent).orElse(null);
                    appointment_answer.getStudents().add(student);
                }

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