package com.app.spoun.services;

import com.app.spoun.domain.Role;
import com.app.spoun.dto.RoleDTO;
import com.app.spoun.mappers.RoleMapper;
import com.app.spoun.mappers.RoleMapperImpl;
import com.app.spoun.repository.IRoleRepository;
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

        // get page of roles
        Pageable page = PageRequest.of(idPage, size);
        Page<Role> roles = iRoleRepository.findAll(page);

        // map all roles
        List<RoleDTO> listRoleDTOS = new ArrayList<>();
        for(Role role : roles){
            RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
            listRoleDTOS.add(roleDTO);
        }
        Page<RoleDTO> roleDTOS = new PageImpl<>(listRoleDTOS);

        // return page of roles
        if(roleDTOS.getSize() != 0){
            answer.put("message", roleDTOS);
        }else {
            answer.put("error", "No role found");
        }
        return answer;
    }

    public Map<String,Object> findRoleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Role role = iRoleRepository.findById(id).orElse(null);
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
        if(roleDTO != null){
            answer.put("message", roleDTO);
        }else{
            answer.put("error", "Role not found");
        }
        return answer;
    }

    public Map<String,Object> saveRole(RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roleDTO != null){
            // save role
            Role role = roleMapper.roleDTOToRole(roleDTO);
            role.setStudents(new ArrayList<>());
            role.setProfessors(new ArrayList<>());
            role.setPatients(new ArrayList<>());
            role.setAdmins(new ArrayList<>());

            iRoleRepository.save(role);
            answer.put("message", "Role saved successfully");
        }else{
            answer.put("error", "Role not saved");
        }
        return answer;
    }

    public Map<String,Object> editRole(RoleDTO roleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(roleDTO.getId() != null && iRoleRepository.existsById(roleDTO.getId())){
            // update role
            Role role = roleMapper.roleDTOToRole(roleDTO);
            role.setStudents(new ArrayList<>());
            role.setProfessors(new ArrayList<>());
            role.setPatients(new ArrayList<>());
            role.setAdmins(new ArrayList<>());

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
            answer.put("message", "Role deleted successfully");
        }else{
            answer.put("error", "Role not found");
        }
        return answer;
    }

}
