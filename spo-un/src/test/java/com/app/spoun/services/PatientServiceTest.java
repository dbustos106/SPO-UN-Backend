package com.app.spoun.services;

import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private IStudentRepository iStudentRepository;

    @Mock
    private IProfessorRepository iProfessorRepository;

    @Mock
    private IPatientRepository iPatientRepository;

    @Mock
    private IAdminRepository iAdminRepository;

    @Mock
    private IRoleRepository iRoleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setName("Student");

        patient = new Patient();
        patient.setUsername("Laura");
        patient.setPassword("pass");
        patient.setName("Laura Jimenez");
        patient.setAge(34);
        patient.setAntecedents(new ArrayList<>());
        patient.setAppointments(new ArrayList<>());
        patient.setRole(role);

    }

    @Test
    void getAllPatient(){
        Page<Patient> patients = new PageImpl<>(Arrays.asList(patient));
        Mockito.when(iPatientRepository.findAll(any(Pageable.class))).thenReturn(patients);
        assertNotNull(patientService.getAllPatient(0, 10));
    }

    @Test
    void findByPatientId(){
        Mockito.when(iPatientRepository.findById(any(Long.class))).thenReturn(Optional.of(patient));
        assertNotNull(patientService.findPatientById(1L));
    }

    @Test
    void savePatient() {
        Mockito.when(iPatientRepository.save(any(Patient.class))).thenReturn(patient);
        assertNotNull(patientService.savePatient(new PatientDTO()));
    }

    @Test
    void editPatient(){
        assertNotNull(patientService.editPatient(new PatientDTO()));
    }

    @Test
    void deletePatient(){
        assertNotNull(patientService.deletePatient(1L));
    }

}