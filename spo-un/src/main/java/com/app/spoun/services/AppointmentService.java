package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.*;
import com.app.spoun.mappers.*;
import com.app.spoun.mappers.TentativeScheduleMapper;
import com.app.spoun.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.*;

@Transactional
@Service
public class AppointmentService {

    private final IAppointmentRepository iAppointmentRepository;
    private final IRoomRepository iRoomRepository;
    private final IPatientRepository iPatientRepository;
    private final IStudentRepository iStudentRepository;
    private final ITentativeScheduleRepository iTentativeScheduleRepository;
    private final IScheduleRepository iScheduleRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientMapper patientMapper;
    private final TentativeScheduleMapper tentativeScheduleMapper;
    private final TentativeScheduleService tentativeScheduleService;
    private final ScheduleService scheduleService;

    @Autowired
    public AppointmentService(IAppointmentRepository iAppointmentRepository,
                              IRoomRepository iRoomRepository,
                              IPatientRepository iPatientRepository,
                              IStudentRepository iStudentRepository,
                              ITentativeScheduleRepository iTentativeScheduleRepository,
                              IScheduleRepository iScheduleRepository,
                              AppointmentMapper appointmentMapper,
                              PatientMapper patientMapper,
                              TentativeScheduleMapper tentativeScheduleMapper,
                              TentativeScheduleService tentativeScheduleService,
                              ScheduleService scheduleService){
        this.iAppointmentRepository = iAppointmentRepository;
        this.iRoomRepository = iRoomRepository;
        this.iPatientRepository = iPatientRepository;
        this.iStudentRepository = iStudentRepository;
        this.iTentativeScheduleRepository = iTentativeScheduleRepository;
        this.iScheduleRepository = iScheduleRepository;
        this.appointmentMapper = appointmentMapper;
        this.patientMapper = patientMapper;
        this.tentativeScheduleMapper = tentativeScheduleMapper;
        this.tentativeScheduleService = tentativeScheduleService;
        this.scheduleService = scheduleService;
    }


    public Map<String, Object> confirmAppointmentByAppointmentId(Long appointmentId, Long patientId, ScheduleDTO scheduleDTO) throws ParseException{
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(appointmentId).orElse(null);
        if(appointment == null){
            throw new NotFoundException("Appointment not found");
        }

        List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
        if(!scheduleService.isAvailableSchedule(schedules, scheduleDTO.getStart_time(), scheduleDTO.getEnd_time())){
            throw new IllegalStateException("Schedule not available");
        }
        // set start_time and end_time
        appointment.setStart_time(scheduleDTO.getStart_time());
        appointment.setEnd_time(scheduleDTO.getEnd_time());
        appointment.setState("Confirmed");

        // get patient
        Patient patient = iPatientRepository.findById(patientId).orElse(null);
        appointment.setPatient(patient);

        // save schedule
        Room room = appointment.getRoom();
        scheduleDTO.setRoom_id(room.getId());
        scheduleService.saveSchedule(scheduleDTO);

        // save appointment
        iAppointmentRepository.save(appointment);
        answer.put("message", "Appointment schedule changed successfully");

        return answer;
    }

    public Map<String, Object> getAllAppointment(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

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
        answer.put("message", appointmentDTOS);

        return answer;
    }

    public Map<String, Object> getAllAvailableAppointment() throws ParseException{
        Map<String, Object> answer = new TreeMap<>();

        // create list object FullAppointmentDTO
        List<FullAppointmentDTO> listFullAppointmentDTOS = new ArrayList<>();

        // get list of appointments
        List<Appointment> appointments = iAppointmentRepository.findAllAvailable();

        // map all appointments
        for(Appointment appointment : appointments){
            FullAppointmentDTO fullAppointmentDTO = new FullAppointmentDTO();
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            fullAppointmentDTO.setAppointmentDTO(appointmentDTO);

            // get tentative schedules
            List<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.findByAppointment_id(appointment.getId());

            // map tentative schedules
            List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
            List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
            for(TentativeSchedule tentativeSchedule : tentativeSchedules){
                if(scheduleService.isAvailableSchedule(schedules, tentativeSchedule.getStart_time(), tentativeSchedule.getEnd_time())){
                    TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
                    listTentativeScheduleDTOS.add(tentativeScheduleDTO);
                }
            }
            fullAppointmentDTO.setTentativeScheduleDTOS(listTentativeScheduleDTOS);

            // get students
            List<Student> students = iStudentRepository.findByAppointment_id(appointment.getId());
            List<String> studentEmails = new ArrayList<>();
            for(Student student : students){
                studentEmails.add(student.getEmail());
            }
            fullAppointmentDTO.setStudents(studentEmails);

            // get professor
            Professor professor = appointment.getProfessor();
            fullAppointmentDTO.setProfessor(professor.getName());

            // get building
            Building building = appointment.getRoom().getBuilding();
            fullAppointmentDTO.setBuilding(building.getName());

            // get room
            Room room = appointment.getRoom();
            fullAppointmentDTO.setRoom(room.getName());

            listFullAppointmentDTOS.add(fullAppointmentDTO);
        }

        // return page of appointments
        answer.put("message", listFullAppointmentDTOS);

        return answer;
    }

    public Map<String, Object> findAppointmentById(Long id) throws ParseException{
        Map<String, Object> answer = new TreeMap<>();

        // create object Appointment_ScheduleDTO
        FullAppointmentDTO fullAppointmentDTO = new FullAppointmentDTO();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);
        if(appointment == null) {
            throw new NotFoundException("No appointment found");
        }
        // map appointment
        AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
        fullAppointmentDTO.setAppointmentDTO(appointmentDTO);

        // get tentative schedules
        List<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.findByAppointment_id(id);

        // map tentative schedules
        List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
        List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
        for(TentativeSchedule tentativeSchedule : tentativeSchedules){
            if(scheduleService.isAvailableSchedule(schedules, tentativeSchedule.getStart_time(), tentativeSchedule.getEnd_time())){
                TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
                listTentativeScheduleDTOS.add(tentativeScheduleDTO);
            }
        }
        fullAppointmentDTO.setTentativeScheduleDTOS(listTentativeScheduleDTOS);

        // get students
        List<Student> students = iStudentRepository.findByAppointment_id(id);
        List<String> studentEmails = new ArrayList<>();
        for(Student student : students){
            studentEmails.add(student.getEmail());
        }
        fullAppointmentDTO.setStudents(studentEmails);

        // get professor
        Professor professor = appointment.getProfessor();
        fullAppointmentDTO.setProfessor(professor.getEmail());

        // get patient
        Patient patient = appointment.getPatient();
        if(patient != null){
            PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
            fullAppointmentDTO.setPatientDTO(patientDTO);
        }

        // get building
        Building building = appointment.getRoom().getBuilding();
        fullAppointmentDTO.setBuilding(building.getName());

        // get room
        Room room = appointment.getRoom();
        fullAppointmentDTO.setRoom(room.getName());

        answer.put("message", fullAppointmentDTO);

        return answer;

    }

    public Map<String, Object> qualifyAppointment(Long id, AppointmentRatingDTO appointmentRatingDTO){
        Map<String, Object> answer = new TreeMap<>();

        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);
        if(appointment == null){
            throw new NotFoundException("No appointment found");
        }
        appointment.setPatient_feedback(appointmentRatingDTO.getPatient_feedback());
        appointment.setPatient_rating(appointmentRatingDTO.getPatient_rating());
        iAppointmentRepository.save(appointment);
        answer.put("message", "Appointment rated successfully");

        return answer;
    }

    public Map<String, Object> saveAppointment(FullAppointmentDTO fullAppointment_DTO) throws ParseException {
        Map<String, Object> answer = new TreeMap<>();

        if(fullAppointment_DTO == null){
            throw new IllegalStateException("Request data missing");
        }
        // extract objects in Appointment_scheduleDTO
        AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointmentDTO();
        List<TentativeScheduleDTO> tentativeScheduleDTOS = fullAppointment_DTO.getTentativeScheduleDTOS();
        List<String> students = fullAppointment_DTO.getStudents();

        // get room and professor
        Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
        Student studentMain = iStudentRepository.findByEmail(students.get(0)).orElse(null);
        Professor professor = studentMain.getProfessor();

        // save appointment
        Appointment appointment = appointmentMapper.appointmentDTOToAppointment(appointmentDTO);
        appointment.setRoom(room);
        appointment.setPatient(null);
        appointment.setState("Available");
        appointment.setProfessor(professor);
        appointment.setStudents(new ArrayList<>());
        appointment.setTentativeSchedules(new ArrayList<>());
        Appointment appointment_answer = iAppointmentRepository.save(appointment);

        // save tentative schedule
        int tentativeSchedulesAvailable = 0;
        List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
        for(TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS){
            if(scheduleService.isAvailableSchedule(schedules, tentativeScheduleDTO.getStart_time(), tentativeScheduleDTO.getEnd_time())) {
                tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
                tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
                tentativeSchedulesAvailable += 1;
            }
        }

        // check the number of schedule available
        if(tentativeSchedulesAvailable == 0){
            throw new IllegalStateException("No schedule is available");
        }

        // save students
        for(String studentEmail : students){
            Student student = iStudentRepository.findByEmail(studentEmail).orElse(null);
            appointment_answer.getStudents().add(student);
        }
        answer.put("message", "Appointment saved successfully");

        return answer;
    }

    public Map<String, Object> editAppointment(FullAppointmentDTO fullAppointment_DTO){
        Map<String, Object> answer = new TreeMap<>();

        if(fullAppointment_DTO == null){
            throw new IllegalStateException("Request data missing");
        }
        // extract objects in fullAppointmentDTO
        AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointmentDTO();
        List<TentativeScheduleDTO> tentativeScheduleDTOS = fullAppointment_DTO.getTentativeScheduleDTOS();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(appointmentDTO.getId()).orElse(null);
        if(appointment == null) {
            throw new NotFoundException("Appointment not found");
        }
        // get room and professor
        Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);

        // if patient is null, update procedure type
        if(appointment.getPatient() == null){
            appointment.setProcedure_type(appointmentDTO.getProcedure_type());
        }

        // update appointment
        appointment.setRoom(room);
        appointment.setStudents(new ArrayList<>());
        appointment.setTentativeSchedules(new ArrayList<>());
        Appointment appointment_answer = iAppointmentRepository.save(appointment);

        // delete tentative schedules
        iTentativeScheduleRepository.deleteByAppointment_id(appointment_answer.getId());

        // update tentative schedule
        for(TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS){
            tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
            tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
        }
        answer.put("message", "Appointment updated successfully");

        return answer;
    }

    public Map<String, Object> deleteAppointment(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(!iAppointmentRepository.existsById(id)){
            throw new NotFoundException("Appointment not found");
        }
        iAppointmentRepository.deleteById(id);
        answer.put("message", "Appointment deleted successfully");

        return answer;
    }

}