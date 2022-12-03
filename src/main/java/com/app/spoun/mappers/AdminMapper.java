package com.app.spoun.mappers;

import com.app.spoun.domain.Admin;
import com.app.spoun.dto.AdminDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(source = "role.id", target = "role_id")
    public AdminDTO adminToAdminDTO(Admin admin);

    @Mapping(source = "role_id", target = "role.id")
    public Admin adminDTOToAdmin(AdminDTO adminDTO);

}
