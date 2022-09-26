package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.StudentDTO;
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
class StudentServiceTest {

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
    private StudentService studentService;

    private Professor professor;

    private Role role;

    private Student student;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        professor = new Professor();
        professor.setPassword("pass");
        professor.setName("Juana");
        professor.setDocument_type("cc");
        professor.setDocument_number("321");

        role = new Role();
        role.setName("Student");

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
    void addRoleToStudent(){
        Mockito.when(iStudentRepository.findByUsername(any(String.class))).thenReturn(Optional.of(student));
        Mockito.when(iRoleRepository.findByName(any(String.class))).thenReturn(Optional.of(role));
        assertNotNull(studentService.addRoleToStudent("Julian", "Student"));
    }

    @Test
    void getAllStudent(){
        Page<Student> students = new PageImpl<>(Arrays.asList(student));
        Mockito.when(iStudentRepository.findAll(any(Pageable.class))).thenReturn(students);
        assertNotNull(studentService.getAllStudent(0, 10));
    }

    @Test
    void findStudentById(){
        Mockito.when(iStudentRepository.findById(any(Integer.class))).thenReturn(Optional.of(student));
        assertNotNull(studentService.findStudentById(1));
    }

    @Test
    void saveStudent() {
        Mockito.when(iStudentRepository.save(any(Student.class))).thenReturn(student);
        assertNotNull(studentService.saveStudent(new StudentDTO()));
    }

    @Test
    void editStudent(){
        assertNotNull(studentService.editStudent(new StudentDTO()));
    }

    @Test
    void deleteStudent(){
        assertNotNull(studentService.deleteStudent(1));
    }

}