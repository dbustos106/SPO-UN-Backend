package com.app.spoun.services;

import com.app.spoun.domain.Role;
import com.app.spoun.dto.RoleDTO;
import com.app.spoun.mappers.RoleMapper;
import com.app.spoun.repository.IRoleRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
@Slf4j
public class RoleService {

    private final IRoleRepository iRoleRepository;
    private final RoleMapper roleMapper;

    @Autowired
    public RoleService(IRoleRepository iRoleRepository,
                       RoleMapper roleMapper){
        this.iRoleRepository = iRoleRepository;
        this.roleMapper = roleMapper;
    }


    public Map<String, Object> getAllRole(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

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
        answer.put("message", roleDTOS);

        return answer;
    }

    public Map<String, Object> findRoleById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Role role = iRoleRepository.findById(id).orElse(null);
        if(role == null){
            throw new NotFoundException("Role not found");
        }
        RoleDTO roleDTO = roleMapper.roleToRoleDTO(role);
        answer.put("message", roleDTO);

        return answer;
    }

    public Map<String, Object> saveRole(RoleDTO roleDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(roleDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        // save role
        Role role = roleMapper.roleDTOToRole(roleDTO);
        role.setStudents(new ArrayList<>());
        role.setProfessors(new ArrayList<>());
        role.setPatients(new ArrayList<>());
        role.setAdmins(new ArrayList<>());

        iRoleRepository.save(role);
        answer.put("message", "Role saved successfully");

        return answer;
    }

    public Map<String, Object> editRole(RoleDTO roleDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(roleDTO == null){
            throw new IllegalStateException("Request data missing");
        }

        Role role = iRoleRepository.findById(roleDTO.getId()).orElse(null);
        if(role == null){
            throw new NotFoundException("Role not found");
        }
        // update role
        role.setName(roleDTO.getName());

        iRoleRepository.save(role);
        answer.put("message", "Role updated successfully");

        return answer;
    }

    public Map<String, Object> deleteRole(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(!iRoleRepository.existsById(id)){
            throw new NotFoundException("Role not found");
        }
        iRoleRepository.deleteById(id);
        answer.put("message", "Role deleted successfully");

        return answer;
    }

}
