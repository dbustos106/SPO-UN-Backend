package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TentativeScheduleDTO {

    private Long id;

    private String start_time;

    private String end_time;

    private Long appointment_id;

}
