package com.app.spoun.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class AppointmentRatingDTO {

    private String patient_feedback;

    private Integer patient_rating;

}
