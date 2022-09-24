package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.dto.AdminDTO;
import com.app.spoun.repository.IAdminRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.webservices.server.WebServiceServerTest;

import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceTest {

    @Autowired
    private IAdminRepository iAdminRepository;
    @Autowired
    private AdminService underTest;

    @Test
    void canSaveAdmin() {

        // given
        String user = "juan";
        String password = "04986";
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setUsername(user);
        adminDTO.setPassword(password);
        String key;
        boolean pass = false;


        // when
        Map<String,Object> answer = underTest.saveAdmin(adminDTO);

        if(answer.containsKey("message")) {
            pass = true;
        }

        // then
        assertThat(pass);

    }

    @Test
    void itShouldAddRoleToAdmin() {

    }

    @Test
    void getAllAdmin() {
    }

    @Test
    void findById() {
    }

    @Test
    void editAdmin() {
    }

    @Test
    void deleteAdmin() {
    }
}