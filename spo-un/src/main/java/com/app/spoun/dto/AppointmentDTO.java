package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppointmentDTO {

    private Integer id;

    private String start_time;

    private String end_time;

    private String procedure_type;

    private String state;

    private String cancel_reason;

    private Integer patient_rating;

    private String patient_feedback;

    private Integer room_id;

    private Integer patient_id;

    private Integer professor_id;

}