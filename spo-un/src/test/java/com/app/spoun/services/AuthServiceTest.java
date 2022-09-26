package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Student;
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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        admin = new Admin();
        admin.setUsername("Admin");
        admin.setPassword("pass");
        admin.setRoles(new ArrayList<>());

        patient = new Patient();
        patient.setUsername("Laura");
        patient.setPassword("pass");
        patient.setName("Laura Jimenez");
        patient.setAge(34);
        patient.setAntecedents(new ArrayList<>());
        patient.setAppointments(new ArrayList<>());
        patient.setRoles(new ArrayList<>());

        professor = new Professor();
        professor.setPassword("pass");
        professor.setName("Juana");
        professor.setDocument_type("cc");
        professor.setDocument_number("321");
        professor.setRoles(new ArrayList<>());

        student = new Student();
        student.setPassword("pass");
        student.setName("Julian");
        student.setDocument_type("cc");
        student.setDocument_number("1234");
        student.setRoles(new ArrayList<>());
        student.setProfessor(new Professor());
        student.setAppointments(new ArrayList<>());

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