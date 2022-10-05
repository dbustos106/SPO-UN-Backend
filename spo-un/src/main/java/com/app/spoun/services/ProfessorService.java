package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.ProfessorMapper;
import com.app.spoun.mappers.ProfessorMapperImpl;
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
public class ProfessorService {

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    private ProfessorMapper professorMapper = new ProfessorMapperImpl();

    private StudentMapper studentMapper = new StudentMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String, Object> getProfessorScheduleByProfessorId(Integer id){
        Map<String, Object> answer = new TreeMap<>();

        // get appointments
        List<Appointment> appointments = iAppointmentRepository.getProfessorScheduleByProfessorId(id);

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

    public Map<String,Object> getAllProfessor(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of professors
        Pageable page = PageRequest.of(idPage, size);
        Page<Professor> professors = iProfessorRepository.findAll(page);

        // map all professors
        List<ProfessorDTO> listProfessorDTOS = new ArrayList<>();
        for(Professor professor : professors){
            ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
            listProfessorDTOS.add(professorDTO);
        }
        Page<ProfessorDTO> professorDTOS = new PageImpl<>(listProfessorDTOS);

        // return page of professors
        if(professorDTOS.getSize() != 0){
            answer.put("message", professorDTOS);
        }else {
            answer.put("error", "No professor found");
        }
        return answer;
    }

    public Map<String,Object> findProfessorById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Professor professor = iProfessorRepository.findById(id).orElse(null);
        ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
        if(professorDTO != null){
            answer.put("message", professorDTO);
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

    public Map<String,Object> getStudentsByProfessorId(Integer idPage, Integer size, Integer Id){
        Map<String,Object> answer = new TreeMap<>();

        // get page of students
        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findByProfessor_id(Id, page);

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
            answer.put("error", "No students found under this professor");
        }
        return answer;
    }

    public Map<String,Object> saveProfessor(ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(professorDTO != null){
            if(iStudentRepository.existsByUsername(professorDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(professorDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(professorDTO.getUsername())){
                answer.put("error", "Repeated username");
            }else {
                // get role
                Role role = iRoleRepository.findByName("Professor").orElse(null);

                // save professor
                Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
                professor.setRole(role);
                professor.setStudents(new ArrayList<>());
                professor.setAppointments(new ArrayList<>());

                // encrypt password
                professor.setPassword(passwordEncoder.encode(professor.getPassword()));

                iProfessorRepository.save(professor);
                answer.put("message", "Professor saved successfully");
            }
        }else{
            answer.put("error", "Professor not saved");
        }
        return answer;
    }

    public Map<String,Object> editProfessor(ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(professorDTO.getId() != null && iProfessorRepository.existsById(professorDTO.getId())){
            // get role
            Role role = iRoleRepository.findByName("Professor").orElse(null);

            // update professor
            Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
            professor.setRole(role);
            professor.setStudents(new ArrayList<>());
            professor.setAppointments(new ArrayList<>());

            // encrypt password
            professor.setPassword(passwordEncoder.encode(professor.getPassword()));

            iProfessorRepository.save(professor);
            answer.put("message", "Professor updated successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

    public Map<String,Object> deleteProfessor(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iProfessorRepository.existsById(id)){
            iProfessorRepository.deleteById(id);
            answer.put("message", "Professor deleted successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

}

