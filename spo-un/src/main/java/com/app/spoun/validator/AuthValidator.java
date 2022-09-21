package com.app.spoun.validator;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.UserDetails;
import com.app.spoun.exceptions.Apiunauthorized;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.util.Objects;

@Component
public class AuthValidator {

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    private static final String CLIENT_CREDENTIALS = "client_credentials";

    public void validate(MultiValueMap<String, String> paramMap, String grantType) throws Apiunauthorized {
        if(grantType.isEmpty() || !grantType.equals(CLIENT_CREDENTIALS)){
            message("The grantType field is invalid");
        }

        if(Objects.isNull(paramMap) ||
                paramMap.getFirst("client_id").isEmpty() ||
                paramMap.getFirst("client_secret").isEmpty()){
            message("client_id and/or client_secret are invalid");
        }
    }

    public UserDetails authenticate(String clientId, String clientSecret) throws Apiunauthorized{

        UserDetails userDetails = new UserDetails();

        // encrypt password
        //Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //String hashSecret = argon2.hash(1, 1024, 1, clientSecret);
        String hashSecret = clientSecret;

        // search for user in the db
        Student student = iStudentRepository.findByUsername(clientId).orElse(null);
        if(student != null) {
            if(student.getPassword().equals(hashSecret)) {
                userDetails.setUsername(student.getUsername());
                userDetails.setRole("Student");
                userDetails.setName(student.getName());
            }
        }else{
            Professor professor = iProfessorRepository.findByUsername(clientId).orElse(null);
            if(professor != null){
                if(professor.getPassword().equals(hashSecret)){
                    userDetails.setUsername(professor.getUsername());
                    userDetails.setRole("Professor");
                    userDetails.setName(professor.getName());
                }
            }else{
                Patient patient = iPatientRepository.findByUsername(clientId).orElse(null);
                if(patient != null){
                    if(patient.getPassword().equals(hashSecret)) {
                        userDetails.setUsername(patient.getUsername());
                        userDetails.setRole("Patient");
                        userDetails.setName(patient.getName());
                    }
                }else{
                    Admin admin = iAdminRepository.findByUsername(clientId).orElse(null);
                    if(admin != null){
                        if(admin.getPassword().equals(hashSecret)) {
                            userDetails.setUsername(admin.getUsername());
                            userDetails.setRole("Admin");
                            userDetails.setName(admin.getUsername());
                        }
                    }else{
                        message("The username does not exist");
                    }
                }
            }
        }

        if(userDetails.getRole() == null){
            message("The credentials are incorrect");
        }

        return userDetails;
    }

    private void message(String message) throws Apiunauthorized{
        throw new Apiunauthorized(message);
    }

}
