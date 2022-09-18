package com.app.spoun.mappers;

import com.app.spoun.dao.AdminDAO;
import com.app.spoun.dto.AdminDTO;
import org.mapstruct.Mapper;

@Mapper
public interface AdminMapper {

    public AdminDTO adminDAOToAdminDTO(AdminDAO adminDAO);

    public AdminDAO adminDTOToAdminDAO(AdminDTO adminDTO);

}
