package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FullAppointmentDTO {

    private AppointmentDTO appointment;

    private List<TentativeScheduleDTO> tentativeSchedules;

    private List<String> students;

}