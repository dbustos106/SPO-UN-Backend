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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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

    @Autowired
    public AuthService(IStudentRepository iStudentRepository,
                       IProfessorRepository iProfessorRepository,
                       IPatientRepository iPatientRepository,
                       IAdminRepository iAdminRepository,
                       JwtIOPropieties jwtIOPropieties){
        this.iStudentRepository = iStudentRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iPatientRepository = iPatientRepository;
        this.iAdminRepository = iAdminRepository;
        this.jwtIOPropieties = jwtIOPropieties;
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

}
