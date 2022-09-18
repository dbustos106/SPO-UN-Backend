package com.app.spoun.mappers;

import com.app.spoun.dao.Student_AppointmentDAO;
import com.app.spoun.dto.Student_AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface Student_AppointmentMapper {

    @Mapping(source = "student.id", target = "student_id")
    @Mapping(source = "appointment.id", target = "appointment_id")
    public Student_AppointmentDTO student_AppointmentDAOToStudent_AppointmentDTO(Student_AppointmentDAO student_AppointmentDAO);

    @Mapping(source = "student_id", target = "student.id")
    @Mapping(source = "appointment_id", target = "appointment.id")
    public Student_AppointmentDAO student_AppointmentDTOToStudent_AppointmentDAO(Student_AppointmentDTO student_AppointmentDTO);

}