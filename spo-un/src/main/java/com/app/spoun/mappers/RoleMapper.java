package com.app.spoun.mappers;

import com.app.spoun.domain.Role;
import com.app.spoun.dto.RoleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    public RoleDTO roleToRoleDTO(Role role);

    @Mapping(target = "admins", ignore = true)
    @Mapping(target = "professors", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "patients", ignore = true)
    public Role roleDTOToRole(RoleDTO roleDTO);

}
