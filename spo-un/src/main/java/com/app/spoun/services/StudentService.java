package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.StudentMapper;
import com.app.spoun.mappers.StudentMapperImpl;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
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
public class StudentService {
    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    private StudentMapper studentMapper = new StudentMapperImpl();

    public Map<String,Object> getAllStudent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Student> studentsDAO = iStudentRepository.findAll(page);

        List<StudentDTO> listStudentsDTO = new ArrayList<>();
        for(Student student : studentsDAO){
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
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);
            Student student = studentMapper.studentDTOToStudent(studentDTO);
            student.setProfessor(professor);

            // encrypt password
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hashPasword = argon2.hash(1, 1024, 1, student.getPassword());
            student.setPassword(hashPasword);

            iStudentRepository.save(student);
            answer.put("message", "Student saved successfully");
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