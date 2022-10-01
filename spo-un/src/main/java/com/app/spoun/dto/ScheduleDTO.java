package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScheduleDTO {

    private Integer id;

    private String start_time;

    private String end_time;

    private Integer appointment_id;

}
