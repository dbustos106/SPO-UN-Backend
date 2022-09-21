package com.app.spoun.security;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Component
public class InterceptorJWTIO implements HandlerInterceptor {

    @Value("${jms.jwt.token.auth.path}")
    private String AUTH_PATH;

    @Value("#{'${jms.jwt.excluted.path}'.split(',')}")
    private List<String> excluted;

    @Autowired
    private JwtIO jwtIO;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        boolean validate = false;
        String url = request.getRequestURI();

        if(url.equals(AUTH_PATH) || excluted(url)){
            validate = true;
        }

        if(!validate && request.getHeader("Authorization") != null && !request.getHeader("Authorization").isEmpty()){
            String token = request.getHeader("Authorization").replace("Bearer", "");
            validate = jwtIO.validateToken(token);

            if(validate) {

                // Get the payload of the request
                String username = jwtIO.extractUsername(token);
                String role = jwtIO.extractRole(token);

                if(role.equals("Student")){
                    Student student = iStudentRepository.findByUsername(username).orElse(null);
                    if(student == null){
                        validate = false;
                    }
                } else if (role.equals("Professor")) {
                    Professor professor = iProfessorRepository.findByUsername(username).orElse(null);
                    if(professor == null){
                        validate = false;
                    }
                }else if(role.equals("Patient")){
                    Patient patient = iPatientRepository.findByUsername(username).orElse(null);
                    if(patient == null){
                        validate = false;
                    }
                }else if(role.equals("Admin")){
                    Admin admin = iAdminRepository.findByUsername(username).orElse(null);
                    if(admin == null){
                        validate = false;
                    }
                }else{
                    validate = false;
                }
            }
        }

        if(!validate){
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
        }

        return validate;
    }

    private boolean excluted(String path){
        boolean result = false;
        for(String exc: excluted){
            if(!exc.equals("#") && exc.equals(path)){
                result = true;
            }
        }
        return result;
    }

}
