package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.ScheduleMapper;
import com.app.spoun.mappers.ScheduleMapperImpl;
import com.app.spoun.mappers.StudentMapper;
import com.app.spoun.mappers.StudentMapperImpl;
import com.app.spoun.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class StudentService {
    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IScheduleRepository iScheduleRepository;

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    private StudentMapper studentMapper = new StudentMapperImpl();

    private ScheduleMapper scheduleMapper = new ScheduleMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> getStudentConfirmedScheduleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        List<Appointment> appointments = iAppointmentRepository.getStudentConfirmedScheduleById(id);

        List<Map<String, Object>> listConfirmedSchedulesDTO = new ArrayList<>();
        for(Appointment appointment : appointments){
            Map<String,Object> confirmedSchedule = new TreeMap<>();
            confirmedSchedule.put("start_time", appointment.getStart_time());
            confirmedSchedule.put("end_time", appointment.getEnd_time());
            listConfirmedSchedulesDTO.add(confirmedSchedule);
        }

        if(listConfirmedSchedulesDTO.size() != 0){
            answer.put("message", listConfirmedSchedulesDTO);
        }else{
            answer.put("error", "No confirmed schedule found");
        }
        return answer;
    }

    public Map<String, Object> getStudentScheduleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        List<Schedule> schedules = iScheduleRepository.getStudentScheduleById(id);

        List<ScheduleDTO> listSchedulesDTO = new ArrayList<>();
        for(Schedule schedule : schedules){
            ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
            listSchedulesDTO.add(scheduleDTO);
        }

        if(listSchedulesDTO.size() != 0){
            answer.put("message", listSchedulesDTO);
        }else{
            answer.put("error", "No schedule found");
        }

        return answer;
    }

    public Map<String,Object> getAllStudent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findAll(page);

        List<StudentDTO> listStudentsDTO = new ArrayList<>();
        for(Student student : students){
            StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
            listStudentsDTO.add(studentDTO);
        }
        Page<StudentDTO> studentsDTO = new PageImpl<>(listStudentsDTO);

        if(studentsDTO.getSize() != 0){
            answer.put("message", studentsDTO);
        }else {
            answer.put("error", "No student found");
        }
        return answer;
    }

    public Map<String,Object> findStudentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Student student = iStudentRepository.findById(id).orElse(null);
        StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
        if(studentDTO != null){
            answer.put("message", studentDTO);
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

    public Map<String,Object> saveStudent(StudentDTO studentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(studentDTO != null){
            if(iProfessorRepository.existsByUsername(studentDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(studentDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(studentDTO.getUsername())){
                answer.put("error", "Repeated username");
            }else {
                Role role = iRoleRepository.findByName("Student").orElse(null);
                Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);
                Student student = studentMapper.studentDTOToStudent(studentDTO);
                student.setProfessor(professor);
                student.setRole(role);
                student.setAppointments(new ArrayList<>());

                // encrypt password
                student.setPassword(passwordEncoder.encode(student.getPassword()));

                iStudentRepository.save(student);
                answer.put("message", "Student saved successfully");
            }
        }else{
            answer.put("error", "Student not saved");
        }
        return answer;
    }

    public Map<String,Object> editStudent(StudentDTO studentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(studentDTO.getId() != null && iStudentRepository.existsById(studentDTO.getId())){
            Role role = iRoleRepository.findByName("Student").orElse(null);
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);
            Student student = studentMapper.studentDTOToStudent(studentDTO);
            student.setProfessor(professor);
            student.setRole(role);

            // encrypt password
            student.setPassword(passwordEncoder.encode(student.getPassword()));

            iStudentRepository.save(student);
            answer.put("message", "Student updated successfully");
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

    public Map<String,Object> deleteStudent(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iStudentRepository.existsById(id)){
            iStudentRepository.deleteById(id);
            answer.put("message", "Student deleted successfully");
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

}