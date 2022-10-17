package com.app.spoun.filter;

import com.app.spoun.security.ApplicationUser;
import com.app.spoun.security.JwtIOPropieties;
import com.app.spoun.utils.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtIOPropieties jwtIOPropieties;
    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, JwtIOPropieties jwtIOPropieties){
        this.authenticationManager = authenticationManager;
        this.jwtIOPropieties = jwtIOPropieties;
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();

        String access_token = JWTUtil.createToken(user, jwtIOPropieties.getToken().getSecret(),
                jwtIOPropieties.getIssuer(), jwtIOPropieties.getToken().getAccess_expires_in());
        String refresh_token = JWTUtil.createToken(user, jwtIOPropieties.getToken().getSecret(),
                jwtIOPropieties.getIssuer(), jwtIOPropieties.getToken().getRefresh_expires_in());

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writer().writeValue(response.getOutputStream(), tokens);
    }

}
