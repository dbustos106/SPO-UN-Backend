package com.app.spoun.services;

import com.app.spoun.domain.Role;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.RoleDTO;
import com.app.spoun.mappers.RoleMapper;
import com.app.spoun.mappers.RoleMapperImpl;
import com.app.spoun.repository.IRoleRepository;
import com.app.spoun.repository.IStudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
@Slf4j
public class RoleService {

    @Autowired
    private IRoleRepository iRoleRepository;

    private RoleMapper roleMapper = new RoleMapperImpl();

    public Map<String,Object> getAllRole (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Role> roles = iRoleRepository.findAll(page);

        List<RoleDTO> listRolesDTO = new ArrayList<>();
        for(Role role : roles){
            RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
            listRolesDTO.add(roleDTO);
        }
        Page<RoleDTO> rolesDTO = new PageImpl<>(listRolesDTO);

        if(rolesDTO.getSize() != 0){
            answer.put("roles", rolesDTO);
        }else {
            answer.put("error", "None role found");
        }
        return answer;
    }

    public Map<String,Object> findRoleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Role role = iRoleRepository.findById(id).orElse(null);
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
        if(roleDTO != null){
            answer.put("role", roleDTO);
        }else{
            answer.put("error", "Role not found");
        }
        return answer;
    }

    public Map<String,Object> saveRole(RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roleDTO != null){
            Role role = roleMapper.roleDTOToRole(roleDTO);
            iRoleRepository.save(role);
            answer.put("message", "Role saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editRole(RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roleDTO.getId() != null && iRoleRepository.existsById(roleDTO.getId())){
            Role role = roleMapper.roleDTOToRole(roleDTO);
            iRoleRepository.save(role);
            answer.put("message", "Role updated successfully");
        }else{
            answer.put("error", "Role not found");
        }
        return answer;
    }

    public Map<String,Object> deleteRole(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iRoleRepository.existsById(id)){
            iRoleRepository.deleteById(id);
            answer.put("menssage", "Role deleted successfully");
        }else{
            answer.put("error", "Role not found");
        }
        return answer;
    }

}