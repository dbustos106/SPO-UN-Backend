package com.app.spoun.services;

import com.app.spoun.domain.Student;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import com.app.spoun.utils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class AuthService {

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private JWTUtil jwtUtil;

    public Map<String,Object> login(StudentDTO user){
        Map<String,Object> answer = new TreeMap<>();

        Student userLog = iStudentRepository.findByUsername(user.getUsername()).orElse(null);
        if(userLog != null){
            String tokenJWT = jwtUtil.create(userLog.getUsername(), userLog.getDocument_number());
            answer.put("token", tokenJWT);
        }else{
            answer.put("error", "401");
            answer.put("message", "User not found");
        }
        return answer;
    }
}
