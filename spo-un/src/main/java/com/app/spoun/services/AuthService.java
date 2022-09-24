package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AuthService implements UserDetailsService {

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        Patient patient = iPatientRepository.findByUsername(username).orElse(null);
        if(patient != null){
            patient.getRoles().forEach(role -> {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            });
            return new User(patient.getUsername(), patient.getPassword(), authorities);
        }else{
            Student student = iStudentRepository.findByUsername(username).orElse(null);
            if(student != null){
                student.getRoles().forEach(role -> {
                    authorities.add(new SimpleGrantedAuthority(role.getName()));
                });
                return new User(student.getUsername(), student.getPassword(), authorities);
            }else{
                Professor professor = iProfessorRepository.findByUsername(username).orElse(null);
                if(professor != null){
                    professor.getRoles().forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role.getName()));
                    });
                    return new User(professor.getUsername(), professor.getPassword(), authorities);
                }else{
                    Admin admin = iAdminRepository.findByUsername(username).orElse(null);
                    if(admin != null){
                        admin.getRoles().forEach(role -> {
                            authorities.add(new SimpleGrantedAuthority(role.getName()));
                        });
                        return new User(admin.getUsername(), admin.getPassword(), authorities);
                    }else{
                        throw new UsernameNotFoundException("User not found in the database");
                    }
                }
            }
        }
    }

}
