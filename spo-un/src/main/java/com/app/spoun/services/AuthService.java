package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.JwtResponse;
import com.app.spoun.dto.UserDetails;
import com.app.spoun.exceptions.Apiunauthorized;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import com.app.spoun.security.JwtIO;
import com.app.spoun.utils.DateUtil;
import com.app.spoun.validator.AuthValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtIO jwtIO;

    @Autowired
    private DateUtil dateUtil;

    @Autowired
    private AuthValidator validator;

    @Value("${jms.jwt.token.expiresIn}")
    private int EXPIRES_IN;

    public JwtResponse login(String clientId, String clientSecret) throws Apiunauthorized {

        // UserDetails
        UserDetails userDetails = validator.authenticate(clientId, clientSecret);

        JwtResponse jwt = JwtResponse.builder()
                .tokenType("bearer")
                .accessToken(jwtIO.generateToken(userDetails))
                .issuedAt(dateUtil.getDateMillis() + "")
                .clientId(clientId)
                .expiresIn(EXPIRES_IN)
                .build();

        return jwt;
    }
}
