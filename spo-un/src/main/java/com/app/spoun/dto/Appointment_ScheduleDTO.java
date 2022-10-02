package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Appointment_ScheduleDTO {

    private AppointmentDTO appointment;

    private List<ScheduleDTO> schedules;

    private List<String> students;

}
