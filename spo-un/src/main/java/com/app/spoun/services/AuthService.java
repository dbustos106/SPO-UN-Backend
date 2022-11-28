package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;

import com.app.spoun.dto.GoogleTokenDTO;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;

import com.app.spoun.security.ApplicationUser;
import com.app.spoun.security.JwtIOPropieties;
import com.app.spoun.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Transactional
@Service
@Slf4j
public class AuthService implements UserDetailsService {

    @Lazy
    @Autowired
    private PatientService patientService;

    @Value("${google.clientId}")
    private String googleClientId;

    private final IStudentRepository iStudentRepository;
    private final IProfessorRepository iProfessorRepository;
    private final IPatientRepository iPatientRepository;
    private final IAdminRepository iAdminRepository;
    private final JwtIOPropieties jwtIOPropieties;

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
        }

        Student student = iStudentRepository.findByEmail(email).orElse(null);
        if(student != null){
            if(!student.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            authorities.add(new SimpleGrantedAuthority(student.getRole().getName()));
            return new ApplicationUser(student.getId(), student.getEmail(), student.getPassword(), authorities,
                    true, true, true, true);
        }

        Professor professor = iProfessorRepository.findByEmail(email).orElse(null);
        if(professor != null){
            if(!professor.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            authorities.add(new SimpleGrantedAuthority(professor.getRole().getName()));
            return new ApplicationUser(professor.getId(), professor.getEmail(), professor.getPassword(), authorities,
                    true, true, true, true);
        }

        Admin admin = iAdminRepository.findByEmail(email).orElse(null);
        if(admin != null){
            if(!admin.isEnabled()){
                throw new UsernameNotFoundException("User not enabled");
            }
            authorities.add(new SimpleGrantedAuthority(admin.getRole().getName()));
            return new ApplicationUser(admin.getId(), admin.getEmail(), admin.getPassword(), authorities,
                    true, true, true, true);
        }

        throw new UsernameNotFoundException("User not found in the database");
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

    public void loginPatientWithGoogle(GoogleTokenDTO googleTokenDTO, HttpServletResponse response) throws IOException {
        final NetHttpTransport netHttpTransport = new NetHttpTransport();
        final GsonFactory gsonFactory = GsonFactory.getDefaultInstance();
        GoogleIdTokenVerifier.Builder verifier =
                new GoogleIdTokenVerifier.Builder(netHttpTransport, gsonFactory)
                        .setAudience(Collections.singletonList(googleClientId));
        final GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), googleTokenDTO.getValue());
        final GoogleIdToken.Payload payload = googleIdToken.getPayload();

        Long userId = -1L;
        String userEmail = "";
        String access_token = "";
        String refresh_token = "";
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        if(iPatientRepository.existsByEmail(payload.getEmail())) {
            Patient patient = iPatientRepository.findByEmail(payload.getEmail()).orElse(null);
            authorities.add(new SimpleGrantedAuthority(patient.getRole().getName()));
            userEmail = patient.getEmail();
            userId = patient.getId();
        }

        if(iProfessorRepository.existsByEmail(payload.getEmail())) {
            Professor professor = iProfessorRepository.findByEmail(payload.getEmail()).orElse(null);
            authorities.add(new SimpleGrantedAuthority(professor.getRole().getName()));
            userEmail = professor.getEmail();
            userId = professor.getId();
        }

        if(iStudentRepository.existsByEmail(payload.getEmail())) {
            Student student = iStudentRepository.findByEmail(payload.getEmail()).orElse(null);
            authorities.add(new SimpleGrantedAuthority(student.getRole().getName()));
            userEmail = student.getEmail();
            userId = student.getId();
        }

        if(iAdminRepository.existsByEmail(payload.getEmail())) {
            Admin admin = iAdminRepository.findByEmail(payload.getEmail()).orElse(null);
            authorities.add(new SimpleGrantedAuthority(admin.getRole().getName()));
            userEmail = admin.getEmail();
            userId = admin.getId();
        }

        if(userId != -1) {
            ApplicationUser user = new ApplicationUser(userId, userEmail, "", authorities,
                    true, true, true, true);

            access_token = JWTUtil.createToken(user, jwtIOPropieties.getToken().getSecret(),
                    jwtIOPropieties.getIssuer(), jwtIOPropieties.getToken().getAccess_expires_in());
            refresh_token = JWTUtil.createToken(user, jwtIOPropieties.getToken().getSecret(),
                    jwtIOPropieties.getIssuer(), jwtIOPropieties.getToken().getRefresh_expires_in());

            Map<String, String> tokens = new HashMap<>();
            tokens.put("access_token", access_token);
            tokens.put("refresh_token", refresh_token);

            response.setContentType(APPLICATION_JSON_VALUE);
            new ObjectMapper().writer().writeValue(response.getOutputStream(), tokens);

        }else {
            PatientDTO patientDTO = new PatientDTO();
            patientDTO.setEmail(payload.getEmail());
            patientDTO.setName("");
            patientDTO.setLast_name("");
            patientDTO.setPassword("pass");
            patientDTO.setAge(0);
            patientDTO.setBlood_type("");
            patientDTO.setDocument_type("");
            patientDTO.setGender("");
            patientService.savePatient(patientDTO);
        }
    }

}
