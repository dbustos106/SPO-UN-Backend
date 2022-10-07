package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.AdminDTO;
import com.app.spoun.mappers.AdminMapper;
import com.app.spoun.mappers.AdminMapperImpl;
import com.app.spoun.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AdminService{

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    private AdminMapper adminMapper = new AdminMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String,Object> getAllAdmin(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of admins
        Pageable page = PageRequest.of(idPage, size);
        Page<Admin> admins = iAdminRepository.findAll(page);

        // map all admins
        List<AdminDTO> listAdminDTOS = new ArrayList<>();
        for(Admin admin : admins){
            AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
            listAdminDTOS.add(adminDTO);
        }
        Page<AdminDTO> adminDTOS = new PageImpl<>(listAdminDTOS);

        // return page of admins
        if(adminDTOS.getSize() != 0){
            answer.put("message", adminDTOS);
        }else {
            answer.put("error", "No admin found");
        }
        return answer;
    }

    public Map<String,Object> findAdminById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Admin admin = iAdminRepository.findById(id).orElse(null);
        AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
        if(adminDTO != null){
            answer.put("message", adminDTO);
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> saveAdmin(AdminDTO adminDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(adminDTO != null){
            if(iProfessorRepository.existsByUsername(adminDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(adminDTO.getUsername()) ||
                    iStudentRepository.existsByUsername(adminDTO.getUsername())){
                answer.put("error", "Repeated username");
            }else {
                // get role
                Role role = iRoleRepository.findByName("Admin").orElse(null);

                // save admin
                Admin admin = adminMapper.adminDTOToAdmin(adminDTO);
                admin.setRole(role);

                // encrypt password
                admin.setPassword(passwordEncoder.encode(admin.getPassword()));

                iAdminRepository.save(admin);
                answer.put("message", "Admin saved successfully");
            }
        }else{
            answer.put("error", "Admin not saved");
        }
        return answer;
    }

    public Map<String,Object> editAdmin(AdminDTO adminDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(adminDTO != null && adminDTO.getId() != null && iAdminRepository.existsById(adminDTO.getId())){
            // get role
            Role role = iRoleRepository.findByName("Admin").orElse(null);

            // update admin
            Admin admin = adminMapper.adminDTOToAdmin(adminDTO);
            admin.setRole(role);

            // encrypt password
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));

            iAdminRepository.save(admin);
            answer.put("message", "Admin updated successfully");
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAdmin(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAdminRepository.existsById(id)){
            iAdminRepository.deleteById(id);
            answer.put("message", "Admin deleted successfully");
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

}