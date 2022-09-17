package com.app.spoun.services;

import com.app.spoun.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Student;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class StudentService {
    @Autowired
    private IStudentRepository iStudentRepository;

    public Map<String,Object> getAllStudent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findAll(page);
        if(students.getSize() != 0){
            answer.put("students", students);
        }else {
            answer.put("error", "No student found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Student student = iStudentRepository.findById(id).orElse(null);
        if(student != null){
            answer.put("student", student);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Student not found");
        }
        return answer;
    }

    public Map<String,Object> saveStudent(Student student){
        Map<String,Object> answer = new TreeMap<>();
        if(student != null){
            System.out.println("Guardar student");
            Student student_answer = iStudentRepository.save(student);
            answer.put("student", student_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editStudent(Student student){
        Map<String,Object> answer = new TreeMap<>();
        if(student.getId() != null && iStudentRepository.existsById(student.getId())){
            Student student_answer = iStudentRepository.save(student);
            answer.put("student", student_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Student not found");
        }
        return answer;
    }

    public Map<String,Object> deleteStudent(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iStudentRepository.existsById(id)){
            iStudentRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Student not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iStudentRepository.existsById(id);
    }

}