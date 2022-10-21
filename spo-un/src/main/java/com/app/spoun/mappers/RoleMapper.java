package com.app.spoun.mappers;

import com.app.spoun.domain.Role;
import com.app.spoun.dto.RoleDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    public RoleDTO roleToRoleDTO(Role role);

    public Role roleDTOToRole(RoleDTO roleDTO);

}
