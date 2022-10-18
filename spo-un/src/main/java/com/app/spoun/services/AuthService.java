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

    @Autowired
    private IStudentRepository iStudentRepository;
    @Autowired
    private IProfessorRepository iProfessorRepository;
    @Autowired
    private IPatientRepository iPatientRepository;
    @Autowired
    private IAdminRepository iAdminRepository;
    @Autowired
    private JwtIOPropieties jwtIOPropieties;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Patient patient = iPatientRepository.findByUsername(username).orElse(null);
        if(patient != null){
            if(!patient.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            authorities.add(new SimpleGrantedAuthority(patient.getRole().getName()));
            return new ApplicationUser(patient.getId(), patient.getUsername(), patient.getPassword(), authorities,
                    true, true, true, true);
        }else{
            Student student = iStudentRepository.findByUsername(username).orElse(null);
            if(student != null){
                if(!student.isEnabled()){
                    throw new UsernameNotFoundException("User not enabled");
                }
                authorities.add(new SimpleGrantedAuthority(student.getRole().getName()));
                return new ApplicationUser(student.getId(), student.getUsername(), student.getPassword(), authorities,
                        true, true, true, true);
            }else{
                Professor professor = iProfessorRepository.findByUsername(username).orElse(null);
                if(professor != null){
                    if(!professor.isEnabled()){
                        throw new UsernameNotFoundException("User not enabled");
                    }
                    authorities.add(new SimpleGrantedAuthority(professor.getRole().getName()));
                    return new ApplicationUser(professor.getId(), professor.getUsername(), professor.getPassword(), authorities,
                            true, true, true, true);
                }else{
                    Admin admin = iAdminRepository.findByUsername(username).orElse(null);
                    if(admin != null){
                        if(!admin.isEnabled()){
                            throw new UsernameNotFoundException("User not enabled");
                        }
                        authorities.add(new SimpleGrantedAuthority(admin.getRole().getName()));
                        return new ApplicationUser(admin.getId(), admin.getUsername(), admin.getPassword(), authorities,
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
                String username = subject.substring(0 , subject.indexOf(","));

                ApplicationUser user = (ApplicationUser) loadUserByUsername(username);
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
