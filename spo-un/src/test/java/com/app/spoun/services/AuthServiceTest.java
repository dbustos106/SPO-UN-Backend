package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.repository.IAdminRepository;
import com.app.spoun.repository.IPatientRepository;
import com.app.spoun.repository.IProfessorRepository;
import com.app.spoun.repository.IStudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class AuthServiceTest {

    @Mock
    private IStudentRepository iStudentRepository;

    @Mock
    private IProfessorRepository iProfessorRepository;

    @Mock
    private IPatientRepository iPatientRepository;

    @Mock
    private IAdminRepository iAdminRepository;

    @InjectMocks
    private AuthService authService;

    private Admin admin;

    private Student student;

    private Patient patient;

    private Professor professor;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();

        admin = new Admin();
        admin.setId(1);
        admin.setUsername("Admin");
        admin.setPassword("pass");
        role.setName("Admin");
        admin.setRole(role);

        patient = new Patient();
        patient.setId(1);
        patient.setUsername("Laura");
        patient.setPassword("pass");
        patient.setName("Laura Jimenez");
        patient.setAge(34);
        patient.setAntecedents(new ArrayList<>());
        patient.setAppointments(new ArrayList<>());
        role.setName("Patient");
        patient.setRole(role);

        professor = new Professor();
        professor.setId(1);
        professor.setPassword("pass");
        professor.setName("Juana");
        professor.setDocument_type("cc");
        professor.setDocument_number("321");
        role.setName("Professor");
        professor.setRole(role);

        student = new Student();
        student.setId(1);
        student.setPassword("pass");
        student.setName("Julian");
        student.setDocument_type("cc");
        student.setDocument_number("1234");
        student.setProfessor(new Professor());
        student.setAppointments(new ArrayList<>());
        role.setName("Admin");
        student.setRole(role);

    }

    @Test
    void loadUserByUsername() {
        Mockito.when(iPatientRepository.findByUsername(any(String.class))).thenReturn(Optional.of(patient));
        Mockito.when(iStudentRepository.findByUsername(any(String.class))).thenReturn(Optional.of(student));
        Mockito.when(iProfessorRepository.findByUsername(any(String.class))).thenReturn(Optional.of(professor));
        Mockito.when(iAdminRepository.findByUsername(any(String.class))).thenReturn(Optional.of(admin));
        assertNotNull(authService.loadUserByUsername("Pimi"));
    }

}