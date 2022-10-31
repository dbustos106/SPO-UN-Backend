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

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class AppointmentService {

    private IAppointmentRepository iAppointmentRepository;
    private IRoomRepository iRoomRepository;
    private IPatientRepository iPatientRepository;
    private IStudentRepository iStudentRepository;
    private ITentativeScheduleRepository iTentativeScheduleRepository;
    private IScheduleRepository iScheduleRepository;
    private BuildingMapper buildingMapper;
    private RoomMapper roomMapper;
    private AppointmentMapper appointmentMapper;
    private TentativeScheduleMapper tentativeScheduleMapper;
    private TentativeScheduleService tentativeScheduleService;
    private ScheduleService scheduleService;

    @Autowired
    public AppointmentService(IAppointmentRepository iAppointmentRepository,
                              IRoomRepository iRoomRepository,
                              IPatientRepository iPatientRepository,
                              IStudentRepository iStudentRepository,
                              ITentativeScheduleRepository iTentativeScheduleRepository,
                              IScheduleRepository iScheduleRepository,
                              BuildingMapper buildingMapper,
                              RoomMapper roomMapper,
                              AppointmentMapper appointmentMapper,
                              TentativeScheduleMapper tentativeScheduleMapper,
                              TentativeScheduleService tentativeScheduleService,
                              ScheduleService scheduleService){
        this.iAppointmentRepository = iAppointmentRepository;
        this.iRoomRepository = iRoomRepository;
        this.iPatientRepository = iPatientRepository;
        this.iStudentRepository = iStudentRepository;
        this.iTentativeScheduleRepository = iTentativeScheduleRepository;
        this.iScheduleRepository = iScheduleRepository;
        this.buildingMapper = buildingMapper;
        this.roomMapper = roomMapper;
        this.appointmentMapper = appointmentMapper;
        this.tentativeScheduleMapper = tentativeScheduleMapper;
        this.tentativeScheduleService = tentativeScheduleService;
        this.scheduleService = scheduleService;
    }


    public boolean isAvailableSchedule(List<Schedule> schedules, String start_time, String end_time) throws ParseException{
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for(Schedule schedule : schedules){
            Date start_schedule = new Date(sdf.parse(schedule.getStart_time()).getTime());
            Date end_schedule = new Date(sdf.parse(schedule.getEnd_time()).getTime());
            Date start_tentative = new Date(sdf.parse(start_time).getTime());
            Date end_tentative = new Date(sdf.parse(end_time).getTime());
            if(!((start_tentative.before(start_schedule) && end_tentative.before(start_schedule)) ||
                    (start_schedule.before(start_tentative) && end_schedule.before(start_tentative)))){
                return false;
            }
        }
        return true;
    }

    public Map<String, Object> confirmAppointmentByAppointmentId(Long appointmentId, Long patientId, ScheduleDTO scheduleDTO) throws ParseException{
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(appointmentId).orElse(null);

        if(appointment != null){

            // check if the schedule is available
            List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
            if(isAvailableSchedule(schedules, scheduleDTO.getStart_time(), scheduleDTO.getEnd_time())){

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
            }else{
                answer.put("error", "Schedule not available");
            }
        }else{
            answer.put("error", "Appointment not found");
        }

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
        if(appointmentDTOS.getSize() != 0){
            answer.put("message", appointmentDTOS);
        }else {
            answer.put("error", "No appointment found");
        }
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
                if(isAvailableSchedule(schedules, tentativeSchedule.getStart_time(), tentativeSchedule.getEnd_time())){
                    TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
                    listTentativeScheduleDTOS.add(tentativeScheduleDTO);
                }
            }
            fullAppointmentDTO.setTentativeSchedules(listTentativeScheduleDTOS);

            // get students
            List<Student> students = iStudentRepository.findByAppointment_id(appointment.getId());
            List<String> usernameStudents = new ArrayList<>();
            for(Student student : students){
                usernameStudents.add(student.getUsername());
            }
            fullAppointmentDTO.setStudents(usernameStudents);

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
        if(listFullAppointmentDTOS.size() != 0){
            answer.put("message", listFullAppointmentDTOS);
        }else {
            answer.put("error", "No appointment found");
        }
        return answer;
    }

    public Map<String, Object> findAppointmentById(Long id) throws ParseException{
        Map<String, Object> answer = new TreeMap<>();

        // create object Appointment_ScheduleDTO
        FullAppointmentDTO fullAppointmentDTO = new FullAppointmentDTO();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);

        if(appointment != null){
            // map appointment
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            fullAppointmentDTO.setAppointmentDTO(appointmentDTO);

            // get tentative schedules
            List<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.findByAppointment_id(id);

            // map tentative schedules
            List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
            List<Schedule> schedules = iScheduleRepository.findByRoom_id(appointment.getRoom().getId());
            for(TentativeSchedule tentativeSchedule : tentativeSchedules){
                if(isAvailableSchedule(schedules, tentativeSchedule.getStart_time(), tentativeSchedule.getEnd_time())){
                    TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
                    listTentativeScheduleDTOS.add(tentativeScheduleDTO);
                }
            }
            fullAppointmentDTO.setTentativeSchedules(listTentativeScheduleDTOS);

            // get students
            List<Student> students = iStudentRepository.findByAppointment_id(id);
            List<String> usernameStudents = new ArrayList<>();
            for(Student student : students){
                usernameStudents.add(student.getUsername());
            }
            fullAppointmentDTO.setStudents(usernameStudents);

            // get professor
            Professor professor = appointment.getProfessor();
            fullAppointmentDTO.setProfessor(professor.getName());

            // get building
            Building building = appointment.getRoom().getBuilding();
            fullAppointmentDTO.setBuilding(building.getName());

            // get room
            Room room = appointment.getRoom();
            fullAppointmentDTO.setRoom(room.getName());

            answer.put("message", fullAppointmentDTO);
        }else{
            answer.put("error", "No appointment found");
        }
        return answer;

    }

    public Map<String, Object> saveAppointment(FullAppointmentDTO fullAppointment_DTO){
        Map<String, Object> answer = new TreeMap<>();
        if(fullAppointment_DTO != null){

            // extract objects in Appointment_scheduleDTO
            AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointmentDTO();
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
            appointment.setState("Available");
            appointment.setProfessor(professor);
            appointment.setStudents(new ArrayList<>());
            appointment.setTentativeSchedules(new ArrayList<>());
            Appointment appointment_answer = iAppointmentRepository.save(appointment);

            // save tentative schedule
            for(TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS){
                tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
                tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
            }

            // save students
            for(String studentUsername : students){
                Student student = iStudentRepository.findByUsername(studentUsername).orElse(null);
                appointment_answer.getStudents().add(student);
            }

            answer.put("message", "Appointment saved successfully");
        }else{
            answer.put("error", "Appointment not saved");
        }
        return answer;
    }

    public Map<String, Object> editAppointment(FullAppointmentDTO fullAppointment_DTO){
        Map<String, Object> answer = new TreeMap<>();
        if(fullAppointment_DTO != null){

            // extract objects in fullAppointmentDTO
            AppointmentDTO appointmentDTO = fullAppointment_DTO.getAppointmentDTO();
            List<TentativeScheduleDTO> tentativeScheduleDTOS = fullAppointment_DTO.getTentativeSchedules();
            List<String> students = fullAppointment_DTO.getStudents();

            if(appointmentDTO.getId() != null && iAppointmentRepository.existsById(appointmentDTO.getId())){

                // get appointment
                Appointment appointment = iAppointmentRepository.findById(appointmentDTO.getId()).orElse(null);

                // get room and professor
                Room room = iRoomRepository.findById(appointmentDTO.getRoom_id()).orElse(null);
                Student studentMain = iStudentRepository.findByUsername(students.get(0)).orElse(null);
                Professor professor = studentMain.getProfessor();

                // if patient is null, update procedure type
                if(appointment.getPatient() == null){
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
                for(TentativeScheduleDTO tentativeScheduleDTO : tentativeScheduleDTOS){
                    tentativeScheduleDTO.setAppointment_id(appointment_answer.getId());
                    tentativeScheduleService.saveTentativeSchedule(tentativeScheduleDTO);
                }

                // update students
                for(String usernameStudent : students){
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

    public Map<String, Object> deleteAppointment(Long id){
        Map<String, Object> answer = new TreeMap<>();
        if(iAppointmentRepository.existsById(id)){
            iAppointmentRepository.deleteById(id);
            answer.put("message", "Appointment deleted successfully");
        }else{
            answer.put("error", "Appointment not found");
        }
        return answer;
    }

}