package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.StudentDTO;
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

    private StudentMapper studentMapper = new StudentMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String,Object> addRoleToStudent(String username, String roleName){
        Map<String,Object> answer = new TreeMap<>();

        Student student = iStudentRepository.findByUsername(username).orElse(null);
        Role role = iRoleRepository.findByName(roleName).orElse(null);

        if(student != null && role != null) {
            student.getRoles().add(role);
            answer.put("message", "Role added successfully");
        }else{
            answer.put("error", "Not successful");
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
            answer.put("students", studentsDTO);
        }else {
            answer.put("error", "None student found");
        }
        return answer;
    }

    public Map<String,Object> findStudentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Student student = iStudentRepository.findById(id).orElse(null);
        StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
        if(studentDTO != null){
            answer.put("student", studentDTO);
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
                Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);
                Student student = studentMapper.studentDTOToStudent(studentDTO);
                student.setRoles(new ArrayList<>());
                student.setProfessor(professor);

                // encrypt password
                student.setPassword(passwordEncoder.encode(student.getPassword()));

                Student student_answer = iStudentRepository.save(student);
                answer.put("message", "Student " + student_answer.getId() + " saved successfully");
            }
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editStudent(StudentDTO studentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(studentDTO.getId() != null && iStudentRepository.existsById(studentDTO.getId())){
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);
            Student student = studentMapper.studentDTOToStudent(studentDTO);
            student.setProfessor(professor);
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
            answer.put("menssage", "Student deleted successfully");
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

}