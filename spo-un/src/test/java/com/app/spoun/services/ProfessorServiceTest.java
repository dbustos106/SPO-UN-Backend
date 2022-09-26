package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.ProfessorDTO;
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
class ProfessorServiceTest {

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
    private ProfessorService professorService;

    private Professor professor;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setPassword("pass");
        professor.setName("Juana");
        professor.setDocument_type("cc");
        professor.setDocument_number("321");
        professor.setRoles(new ArrayList<>());

        role = new Role();
        role.setName("Professor");

    }

    @Test
    void addRoleToProfessor(){
        Mockito.when(iProfessorRepository.findByUsername(any(String.class))).thenReturn(Optional.of(professor));
        Mockito.when(iRoleRepository.findByName(any(String.class))).thenReturn(Optional.of(role));
        assertNotNull(professorService.addRoleToProfessor("Juana", "Professor"));
    }

    @Test
    void getAllProfessor(){
        Page<Professor> professors = new PageImpl<>(Arrays.asList(professor));
        Mockito.when(iProfessorRepository.findAll(any(Pageable.class))).thenReturn(professors);
        assertNotNull(professorService.getAllProfessor(0, 10));
    }

    @Test
    void findProfessorById(){
        Mockito.when(iProfessorRepository.findById(any(Integer.class))).thenReturn(Optional.of(professor));
        assertNotNull(professorService.findProfessorById(1));
    }

    @Test
    void saveProfessor() {
        Mockito.when(iProfessorRepository.save(any(Professor.class))).thenReturn(professor);
        assertNotNull(professorService.saveProfessor(new ProfessorDTO()));
    }

    @Test
    void editProfessor(){
        assertNotNull(professorService.editProfessor(new ProfessorDTO()));
    }

    @Test
    void deleteProfessor(){
        assertNotNull(professorService.deleteProfessor(1));
    }

}