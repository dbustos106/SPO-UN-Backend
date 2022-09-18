package com.app.spoun.mappers;

import com.app.spoun.dao.AppointmentDAO;
import com.app.spoun.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface AppointmentMapper {

    @Mapping(source = "room.id", target = "room_id")
    @Mapping(source = "patient.id", target = "patient_id")
    @Mapping(source = "professor.id", target = "professor_id")
    public AppointmentDTO appointmentDAOToAppointmentDTO(AppointmentDAO appointmentDAO);

    @Mapping(source = "room_id", target = "room.id")
    @Mapping(source = "patient_id", target = "patient.id")
    @Mapping(source = "professor_id", target = "professor.id")
    public AppointmentDAO appointmentDTOToAppointmentDAO(AppointmentDTO appointmentDTO);
}
