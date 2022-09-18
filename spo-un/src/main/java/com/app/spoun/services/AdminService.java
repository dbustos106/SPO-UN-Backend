package com.app.spoun.services;

import com.app.spoun.repository.IAdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Admin;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class AdminService {
    @Autowired
    private IAdminRepository iAdminRepository;

    public Map<String,Object> getAllAdmin (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Admin> admins = iAdminRepository.findAll(page);
        if(admins.getSize() != 0){
            answer.put("admins", admins);
        }else {
            answer.put("error", "No admin found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Admin admin = iAdminRepository.findById(id).orElse(null);
        if(admin != null){
            answer.put("admin", admin);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> saveAdmin(Admin admin){
        Map<String,Object> answer = new TreeMap<>();
        if(admin != null){
            System.out.println("Guardar admin");
            Admin admin_answer = iAdminRepository.save(admin);
            answer.put("admin", admin_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editAdmin(Admin admin){
        Map<String,Object> answer = new TreeMap<>();
        if(admin.getId() != null && iAdminRepository.existsById(admin.getId())){
            Admin admin_answer = iAdminRepository.save(admin);
            answer.put("admin", admin_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Admin not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAdmin(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAdminRepository.existsById(id)){
            iAdminRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Admin not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iAdminRepository.existsById(id);
    }

}