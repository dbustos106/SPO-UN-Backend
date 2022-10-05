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

    public Map<String, Object> getStudentScheduleByStudentId(Integer id){
        Map<String,Object> answer = new TreeMap<>();

        // get appointments
        List<Appointment> appointments = iAppointmentRepository.getStudentScheduleByStudentId(id);

        // read schedules
        List<Map<String, Object>> listScheduleDTOS = new ArrayList<>();
        for(Appointment appointment : appointments){
            Map<String,Object> schedule = new TreeMap<>();
            schedule.put("start_time", appointment.getStart_time());
            schedule.put("end_time", appointment.getEnd_time());
            listScheduleDTOS.add(schedule);
        }

        // return schedules
        if(listScheduleDTOS.size() != 0){
            answer.put("message", listScheduleDTOS);
        }else{
            answer.put("error", "No schedule found");
        }
        return answer;
    }

    public Map<String, Object> getStudentUnconfirmedScheduleByStudentId(Integer id){
        Map<String,Object> answer = new TreeMap<>();

        // get schedules
        List<Schedule> schedules = iScheduleRepository.getStudentUnconfirmedScheduleByStudentId(id);

        // map schedules
        List<ScheduleDTO> listScheduleDTOS = new ArrayList<>();
        for(Schedule schedule : schedules){
            ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
            listScheduleDTOS.add(scheduleDTO);
        }

        // return schedules
        if(listScheduleDTOS.size() != 0){
            answer.put("message", listScheduleDTOS);
        }else{
            answer.put("error", "No unconfirmed schedule found");
        }

        return answer;
    }

    public Map<String,Object> getAllStudent(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of students
        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findAll(page);

        // map all students
        List<StudentDTO> listStudentDTOS = new ArrayList<>();
        for(Student student : students){
            StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
            listStudentDTOS.add(studentDTO);
        }
        Page<StudentDTO> studentDTOS = new PageImpl<>(listStudentDTOS);

        // return page of students
        if(studentDTOS.getSize() != 0){
            answer.put("message", studentDTOS);
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
                // get role and professor
                Role role = iRoleRepository.findByName("Student").orElse(null);
                Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);

                // save student
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
            // get role and professor
            Role role = iRoleRepository.findByName("Student").orElse(null);
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);

            // update student
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