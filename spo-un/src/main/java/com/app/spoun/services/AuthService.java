package com.app.spoun.services;

import com.app.spoun.dto.JwtResponse;
import com.app.spoun.security.JwtIO;
import com.app.spoun.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private JwtIO jwtIO;

    @Autowired
    private DateUtil dateUtil;

    @Value("${jms.jwt.token.expiresIn}")
    private int EXPIRES_IN;

    public JwtResponse login(String clientId, String clientSecret){



        JwtResponse jwt = JwtResponse.builder()
                .tokenType("bearer")
                .accessToken(jwtIO.generateToken("Aqui deberia enviar el rol"))
                .issuedAt(dateUtil.getDateMillis() + "")
                .clientId(clientId)
                .expiresIn(EXPIRES_IN)
                .build();

        return jwt;
    }
}
