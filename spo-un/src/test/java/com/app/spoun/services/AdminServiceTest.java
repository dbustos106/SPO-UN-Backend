package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.AdminDTO;
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

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

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
    private AdminService adminService;

    private Admin admin;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        role = new Role();
        role.setName("Professor");

        admin = new Admin();
        admin.setUsername("Admin");
        admin.setPassword("pass");
        admin.setRole(role);

    }

    @Test
    void getAllAdmin(){
        Page<Admin> admins = new PageImpl<>(Arrays.asList(admin));
        Mockito.when(iAdminRepository.findAll(any(Pageable.class))).thenReturn(admins);
        assertNotNull(adminService.getAllAdmin(0, 10));
    }

    @Test
    void findByAdminId(){
        Mockito.when(iAdminRepository.findById(any(Integer.class))).thenReturn(Optional.of(admin));
        assertNotNull(adminService.findAdminById(1));
    }

    @Test
    void saveAdmin(){
        Mockito.when(iAdminRepository.save(any(Admin.class))).thenReturn(admin);
        assertNotNull(adminService.saveAdmin(new AdminDTO()));
    }

    @Test
    void editAdmin(){
        assertNotNull(adminService.editAdmin(new AdminDTO()));
    }

    @Test
    void deleteAdmin(){
        assertNotNull(adminService.deleteAdmin(1));
    }

}