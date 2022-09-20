package com.app.spoun.security;

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
