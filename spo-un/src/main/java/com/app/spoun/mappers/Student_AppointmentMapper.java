package com.app.spoun.mappers;

import com.app.spoun.domain.Student_Appointment;
import com.app.spoun.dto.Student_AppointmentDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface Student_AppointmentMapper {

    @Mapping(source = "student.id", target = "student_id")
    @Mapping(source = "appointment.id", target = "appointment_id")
    public Student_AppointmentDTO student_AppointmentToStudent_AppointmentDTO(Student_Appointment student_Appointment);

    @Mapping(source = "student_id", target = "student.id")
    @Mapping(source = "appointment_id", target = "appointment.id")
    public Student_Appointment student_AppointmentDTOToStudent_Appointment(Student_AppointmentDTO student_AppointmentDTO);

}