package com.app.spoun.filter;

import com.app.spoun.security.JwtIOPropieties;
import com.app.spoun.utils.JWTUtil;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private final JwtIOPropieties jwtIOPropieties;

    public CustomAuthorizationFilter(JwtIOPropieties jwtIOPropieties){
        this.jwtIOPropieties = jwtIOPropieties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/auth/login") || request.getServletPath().equals("/auth/tokenRefresh") ||
                request.getServletPath().equals("/register/patient") || request.getServletPath().equals("/register/verifyAccount/admin") ||
                request.getServletPath().equals("/register/verifyAccount/professor") || request.getServletPath().equals("/register/verifyAccount/student") ||
                request.getServletPath().equals("/register/verifyAccount/patient") || request.getServletPath().equals("/auth/emailToChangePassword") ||
                request.getServletPath().equals("/patient/changePassword") || request.getServletPath().equals("/student/changePassword") ||
                request.getServletPath().equals("/professor/changePassword") || request.getServletPath().equals("/admin/changePassword")){
            filterChain.doFilter(request, response);
        }else{
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
                try {

                    String token = authorizationHeader.substring("Bearer ".length());
                    DecodedJWT decodedJWT = JWTUtil.verifyToken(token, jwtIOPropieties.getToken().getSecret());
                    String subject = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);

                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    authorities.add(new SimpleGrantedAuthority(roles[0]));
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(subject, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);

                }catch (Exception e) {
                    response.setStatus(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writer().writeValue(response.getOutputStream(), error);
                }
            }else{
                filterChain.doFilter(request, response);
            }
        }
    }

}
