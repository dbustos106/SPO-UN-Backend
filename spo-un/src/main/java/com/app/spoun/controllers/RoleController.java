package com.app.spoun.controllers;

import com.app.spoun.dto.RoleDTO;
import com.app.spoun.services.AuthService;
import com.app.spoun.services.RoleService;
import com.app.spoun.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @Value("${jms.jwt.issuer:none}")
    private String ISSUER;

    @Value("${jms.jwt.token.secret:secret}")
    private String SECRET;

    @Value("${jms.jwt.token.access_expires_in:600000}")
    private int ACCESS_EXPIRES_IN;

    @GetMapping(value = "/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                String username = JWTUtil.verifyToken(refresh_token, SECRET).getSubject();

                UserDetails user = authService.loadUserByUsername(username);
                String access_token = JWTUtil.createToken(user, SECRET, ISSUER, ACCESS_EXPIRES_IN);

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

    @GetMapping(value = "/role/all")
    public ResponseEntity<?> getAllRole (
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size){
        Map<String, Object> answer = new TreeMap<>();
        try{
            answer = roleService.getAllRole(page, size);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @GetMapping(value = "/role/{id}")
    public ResponseEntity<?> findRoleById(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.findRoleById(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PostMapping(value = "/role/save")
    public ResponseEntity<?> saveRole(@RequestBody RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.saveRole(roleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @PutMapping(value = "/role/edit")
    public ResponseEntity<?> editRole(@RequestBody RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.editRole(roleDTO);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

    @DeleteMapping(value = "/role/delete/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable("id") Integer id){
        Map<String,Object> answer = new TreeMap<>();
        try{
            answer = roleService.deleteRole(id);
        }catch(Exception e){
            answer.put("error", e);
        }
        return ResponseEntity.ok().body(answer);
    }

}
