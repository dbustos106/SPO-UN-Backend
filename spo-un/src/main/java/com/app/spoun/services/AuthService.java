package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;

import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;

import com.app.spoun.security.ApplicationUser;
import com.app.spoun.security.JwtIOPropieties;
import com.app.spoun.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService implements UserDetailsService {

    private IStudentRepository iStudentRepository;
    private IProfessorRepository iProfessorRepository;
    private IPatientRepository iPatientRepository;
    private IAdminRepository iAdminRepository;
    private JwtIOPropieties jwtIOPropieties;
    private EmailSenderService emailSenderService;

    @Autowired
    public AuthService(IStudentRepository iStudentRepository,
                       IProfessorRepository iProfessorRepository,
                       IPatientRepository iPatientRepository,
                       IAdminRepository iAdminRepository,
                       JwtIOPropieties jwtIOPropieties,
                       EmailSenderService emailSenderService){
        this.iStudentRepository = iStudentRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iPatientRepository = iPatientRepository;
        this.iAdminRepository = iAdminRepository;
        this.jwtIOPropieties = jwtIOPropieties;
        this.emailSenderService = emailSenderService;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Patient patient = iPatientRepository.findByEmail(email).orElse(null);
        if(patient != null){
            if(!patient.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            authorities.add(new SimpleGrantedAuthority(patient.getRole().getName()));
            return new ApplicationUser(patient.getId(), patient.getEmail(), patient.getPassword(), authorities,
                    true, true, true, true);
        }else{
            Student student = iStudentRepository.findByEmail(email).orElse(null);
            if(student != null){
                if(!student.isEnabled()){
                    throw new UsernameNotFoundException("User not enabled");
                }
                authorities.add(new SimpleGrantedAuthority(student.getRole().getName()));
                return new ApplicationUser(student.getId(), student.getEmail(), student.getPassword(), authorities,
                        true, true, true, true);
            }else{
                Professor professor = iProfessorRepository.findByEmail(email).orElse(null);
                if(professor != null){
                    if(!professor.isEnabled()){
                        throw new UsernameNotFoundException("User not enabled");
                    }
                    authorities.add(new SimpleGrantedAuthority(professor.getRole().getName()));
                    return new ApplicationUser(professor.getId(), professor.getEmail(), professor.getPassword(), authorities,
                            true, true, true, true);
                }else{
                    Admin admin = iAdminRepository.findByEmail(email).orElse(null);
                    if(admin != null){
                        if(!admin.isEnabled()){
                            throw new UsernameNotFoundException("User not enabled");
                        }
                        authorities.add(new SimpleGrantedAuthority(admin.getRole().getName()));
                        return new ApplicationUser(admin.getId(), admin.getEmail(), admin.getPassword(), authorities,
                                true, true, true, true);
                    }else{
                        throw new UsernameNotFoundException("User not found in the database");
                    }
                }
            }
        }
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                String subject = JWTUtil.verifyToken(refresh_token, jwtIOPropieties.getToken().getSecret()).getSubject();
                String email = subject.substring(0 , subject.indexOf(","));

                ApplicationUser user = (ApplicationUser) loadUserByUsername(email);
                String access_token = JWTUtil.createToken(user, jwtIOPropieties.getToken().getSecret(),
                        jwtIOPropieties.getIssuer(), jwtIOPropieties.getToken().getAccess_expires_in());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writer().writeValue(response.getOutputStream(), tokens);

            }catch (Exception e) {
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writer().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
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
        String verifyURL = "http://localhost:8080/" + role + "/changePassword/" + randomCode;
        //String verifyURL = "http://spoun.app.s3-website-us-east-1.amazonaws.com/" + role + "/changePassword/" + randomCode;
        content = content.replace("[[URL]]", verifyURL);
        emailSenderService.send(email, subject, content);

        return answer;
    }

}
