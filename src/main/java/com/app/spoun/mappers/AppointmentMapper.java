package com.app.spoun.mappers;

import com.app.spoun.domain.Appointment;
import com.app.spoun.dto.AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(source = "room.id", target = "room_id")
    @Mapping(source = "patient.id", target = "patient_id")
    @Mapping(source = "professor.id", target = "professor_id")
    public AppointmentDTO appointmentToAppointmentDTO(Appointment appointment);

    @Mapping(target = "students", ignore = true)
    @Mapping(target = "tentativeSchedules", ignore = true)
    @Mapping(source = "room_id", target = "room.id")
    @Mapping(source = "patient_id", target = "patient.id")
    @Mapping(source = "professor_id", target = "professor.id")
    public Appointment appointmentDTOToAppointment(AppointmentDTO appointmentDTO);

}
