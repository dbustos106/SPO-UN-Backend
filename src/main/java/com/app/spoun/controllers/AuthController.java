package com.app.spoun.controllers;

import com.app.spoun.dto.GoogleTokenDTO;
import com.app.spoun.services.AuthService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }


    @GetMapping(value = "/refreshToken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);
    }

    @PostMapping(value = "/loginWithGoogle")
    public void loginWithGoogle(
            @RequestBody GoogleTokenDTO googleTokenDTO,
            HttpServletResponse response) throws IOException {
        authService.loginPatientWithGoogle(googleTokenDTO, response);
    }

}
