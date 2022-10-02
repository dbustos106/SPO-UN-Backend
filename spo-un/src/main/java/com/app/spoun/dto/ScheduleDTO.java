package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ScheduleDTO {

    private Integer id;

    private String start_time;

    private String end_time;

    private Integer appointment_id;

}
