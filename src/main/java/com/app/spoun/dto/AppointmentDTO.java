package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppointmentDTO {

    private Long id;

    private String start_time;

    private String end_time;

    private String procedure_type;

    private String state;

    private String cancel_reason;

    private Integer patient_rating;

    private String patient_feedback;

    private Long room_id;

    private Long patient_id;

    private Long professor_id;

}