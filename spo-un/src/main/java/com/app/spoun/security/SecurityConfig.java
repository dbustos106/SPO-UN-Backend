package com.app.spoun.security;

import com.app.spoun.filter.CustomAuthenticationFilter;
import com.app.spoun.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private JwtIOPropieties jwtIOPropieties;
    private UserDetailsService userDetailsService;
    private AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    public SecurityConfig(JwtIOPropieties jwtIOPropieties,
                          UserDetailsService userDetailsService,
                          AuthenticationConfiguration authenticationConfiguration){
        this.jwtIOPropieties = jwtIOPropieties;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter customAuthenticationFilter = new CustomAuthenticationFilter(authenticationManagerBean(authenticationConfiguration), jwtIOPropieties);
        customAuthenticationFilter.setFilterProcessesUrl("/auth/login");

        http.cors().and().csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/auth/login/**",
                "/auth/loginWithGoogle/**",
                "/auth/tokenRefresh/**",
                "/changePassword/**",
                "/register/patient/**",
                "/register/verifyAccount/**").permitAll();

        http.authorizeRequests().antMatchers(GET, "/building/all/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(POST, "/building/save/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(DELETE, "/building/**/delete/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(POST, "/room/save/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(DELETE, "/room/**/delete/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(POST, "/register/professor/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(POST, "/schedule/save/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(GET, "/room/**/schedules/**").hasAnyAuthority("Admin");
        http.authorizeRequests().antMatchers(DELETE, "/schedule/**/delete/**").hasAnyAuthority("Admin");

        http.authorizeRequests().antMatchers(POST, "/register/student/**").hasAnyAuthority("Professor");
        http.authorizeRequests().antMatchers(GET, "/professor/**/schedule/**").hasAnyAuthority("Professor");
        http.authorizeRequests().antMatchers(GET, "/professor/**/appointments/**").hasAnyAuthority("Professor");
        http.authorizeRequests().antMatchers(GET, "/professor/**").hasAnyAuthority("Professor");
        http.authorizeRequests().antMatchers(PUT, "/professor/edit/**").hasAnyAuthority("Professor");
        http.authorizeRequests().antMatchers(GET, "/professor/**/students/**").hasAnyAuthority("Professor");

        http.authorizeRequests().antMatchers(POST, "/appointment/save/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(PUT, "/appointment/edit/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(PUT, "/student/cancelAppointment/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(GET, "/student/**/schedule/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(GET, "/student/**/unconfirmedSchedule/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(GET, "/student/**/appointments/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(GET, "/student/**").hasAnyAuthority("Student");
        http.authorizeRequests().antMatchers(PUT, "/student/edit/**").hasAnyAuthority("Student");

        http.authorizeRequests().antMatchers(PUT, "/appointment/**/qualify/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(PUT, "/patient/cancelAppointment/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(GET, "/patient/**/schedule/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(GET, "/patient/**/appointments/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(PUT, "/appointment/**/confirmPatient/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(GET, "/appointment/allAvailable/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(GET, "/patient/**").hasAnyAuthority("Patient");
        http.authorizeRequests().antMatchers(PUT, "/patient/edit/**").hasAnyAuthority("Patient");

        http.authorizeRequests().antMatchers(GET, "/appointment/**").hasAnyAuthority("Student", "Professor", "Patient");
        http.authorizeRequests().antMatchers(GET, "/room/all/**").hasAnyAuthority("Student", "Admin");

        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(customAuthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(jwtIOPropieties), UsernamePasswordAuthenticationFilter.class);
        http.authenticationProvider(daoAuthenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

}