package com.app.spoun.mappers;

import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScheduleMapper {

    @Mapping(source = "appointment.id", target = "appointment_id")
    public ScheduleDTO scheduleToScheduleDTO(Schedule schedule);

    @Mapping(source = "appointment_id", target = "appointment.id")
    public Schedule scheduleDTOToSchedule(ScheduleDTO scheduleDTO);

}
