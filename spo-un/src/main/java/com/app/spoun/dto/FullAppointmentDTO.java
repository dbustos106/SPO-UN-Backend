package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class FullAppointmentDTO {

    private AppointmentDTO appointmentDTO;

    private List<TentativeScheduleDTO> tentativeSchedules;

    private List<String> students;

    private String professor;

    private String building;

    private String room;

}
