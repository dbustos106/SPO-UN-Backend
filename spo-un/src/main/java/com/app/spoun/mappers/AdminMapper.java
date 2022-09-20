package com.app.spoun.mappers;

import com.app.spoun.domain.Admin;
import com.app.spoun.dto.AdminDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AdminMapper {

    public AdminDTO adminToAdminDTO(Admin admin);

    public Admin adminDTOToAdmin(AdminDTO adminDTO);

}
