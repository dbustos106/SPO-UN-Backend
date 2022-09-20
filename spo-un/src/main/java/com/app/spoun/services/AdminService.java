package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.dto.AdminDTO;
import com.app.spoun.mappers.AdminMapper;
import com.app.spoun.mappers.AdminMapperImpl;
import com.app.spoun.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AdminService {
    @Autowired
    private IAdminRepository iAdminRepository;

    private AdminMapper adminMapper = new AdminMapperImpl();

    public Map<String,Object> getAllAdmin (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Admin> adminsDAO = iAdminRepository.findAll(page);

        List<AdminDTO> listAdminsDTO = new ArrayList<>();
        for(Admin admin : adminsDAO){
            AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
            listAdminsDTO.add(adminDTO);
        }
        Page<AdminDTO> adminsDTO = new PageImpl<>(listAdminsDTO);

        if(adminsDTO.getSize() != 0){
            answer.put("admins", adminsDTO);
        }else {
            answer.put("error", "None admin found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Admin admin = iAdminRepository.findById(id).orElse(null);
        AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
        if(adminDTO != null){
            answer.put("admin", adminDTO);
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> saveAdmin(AdminDTO adminDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(adminDTO != null){
            Admin admin = adminMapper.adminDTOToAdmin(adminDTO);
            iAdminRepository.save(admin);
            answer.put("message", "Admin saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editAdmin(AdminDTO adminDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(adminDTO.getId() != null && iAdminRepository.existsById(adminDTO.getId())){
            Admin admin = adminMapper.adminDTOToAdmin(adminDTO);
            iAdminRepository.save(admin);
            answer.put("message", "Student updated successfully");
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAdmin(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAdminRepository.existsById(id)){
            iAdminRepository.deleteById(id);
            answer.put("menssage", "Student deleted successfully");
        }else{
            answer.put("error", "Admin not found");
        }
        return answer;
    }

}