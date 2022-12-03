package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.AdminDTO;
import com.app.spoun.mappers.AdminMapper;
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

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private IAdminRepository iAdminRepository;
    @Mock
    private IProfessorRepository iProfessorRepository;
    @Mock
    private IStudentRepository iStudentRepository;
    @Mock
    private IPatientRepository iPatientRepository;
    @Mock
    private IRoleRepository iRoleRepository;
    @Mock
    private EmailValidatorService emailValidatorService;
    @Mock
    private EmailSenderService emailSenderService;
    @Mock
    private AdminMapper adminMapper;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;
    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setName("Professor");

        admin = new Admin();
        admin.setId(1L);
        admin.setVerification_code("");
        admin.setEnabled(false);
        admin.setEmail("spoun.app@gmail.com");
        admin.setPassword("password");
        admin.setRole(role);

    }

    /*@Test
    void getAllAdmin(){
        List<Admin> listAdmins = new ArrayList<>();
        listAdmins.add(admin);
        Page<Admin> admins = new PageImpl<>(listAdmins);
        System.out.println(admins.getContent().size());
        //Mockito.when(iAdminRepository.findAll(any(Pageable.class))).thenReturn(admins);


        Mockito.doReturn(admins).when(iAdminRepository).findAll(any(Pageable.class));
        assertNotNull(adminService.getAllAdmin(0, 10));
    }*/
/*
    @Test
    void findByAdminId(){
        Mockito.when(iAdminRepository.findById(any(Long.class))).thenReturn(Optional.of(admin));
        assertNotNull(adminService.findAdminById(1L));
    }

    @Test
    void saveAdmin() throws UnsupportedEncodingException, MessagingException {
        assertNotNull(adminService.saveAdmin(new AdminDTO()));
    }

    @Test
    void editAdmin(){
        assertNotNull(adminService.editAdmin(new AdminDTO()));
    }

    @Test
    void deleteAdmin(){
        assertNotNull(adminService.deleteAdmin(1L));
    }*/

}