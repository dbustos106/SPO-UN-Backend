package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
@Slf4j
public class ChangePasswordService {

    private final IPatientRepository iPatientRepository;
    private final IStudentRepository iStudentRepository;
    private final IProfessorRepository iProfessorRepository;
    private final IAdminRepository iAdminRepository;
    private final EmailSenderService emailSenderService;

    @Autowired
    public ChangePasswordService(IPatientRepository iPatientRepository,
                                 IStudentRepository iStudentRepository,
                                 IProfessorRepository iProfessorRepository,
                                 IAdminRepository iAdminRepository,
                                 EmailSenderService emailSenderService){
        this.iPatientRepository = iPatientRepository;
        this.iStudentRepository = iStudentRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iAdminRepository = iAdminRepository;
        this.emailSenderService = emailSenderService;
    }


    public Map<String, Object> emailToChangePassword(String email) throws UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        String randomCode = "";
        String role = "";

        Patient patient = iPatientRepository.findByEmail(email).orElse(null);
        if(patient != null){
            if(!patient.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            role = "patient";
            randomCode = RandomString.make(64);
            patient.setVerification_code(randomCode);
            iPatientRepository.save(patient);
        }else{
            Student student = iStudentRepository.findByEmail(email).orElse(null);
            if(student != null){
                if(!student.isEnabled()){
                    throw new UsernameNotFoundException("User not enabled");
                }
                role = "student";
                randomCode = RandomString.make(64);
                student.setVerification_code(randomCode);
                iStudentRepository.save(student);
            }else{
                Professor professor = iProfessorRepository.findByEmail(email).orElse(null);
                if(professor != null){
                    if(!professor.isEnabled()){
                        throw new UsernameNotFoundException("User not enabled");
                    }
                    role = "professor";
                    randomCode = RandomString.make(64);
                    professor.setVerification_code(randomCode);
                    iProfessorRepository.save(professor);
                }else{
                    Admin admin = iAdminRepository.findByEmail(email).orElse(null);
                    if(admin != null){
                        if(!admin.isEnabled()){
                            throw new UsernameNotFoundException("User not enabled");
                        }
                        role = "admin";
                        randomCode = RandomString.make(64);
                        admin.setVerification_code(randomCode);
                        iAdminRepository.save(admin);
                    }else{
                        throw new UsernameNotFoundException("User not found in the database");
                    }
                }
            }
        }

        String content = "Haga click en el siguiente link para cambiar su contraseña:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Gracias,<br>"
                + "Spo-un.";
        String subject = "Cambio de contraseña";
        //String verifyURL = "http://localhost:8080/changePassword/" + role + "/" + randomCode;
        String verifyURL = "http://spoun.app.s3-website-us-east-1.amazonaws.com/changePassword/" + role + "/" + randomCode;
        content = content.replace("[[URL]]", verifyURL);
        emailSenderService.send(email, subject, content);

        return answer;
    }

}
