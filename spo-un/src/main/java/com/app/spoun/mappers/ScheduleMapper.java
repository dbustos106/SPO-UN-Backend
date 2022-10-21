package com.app.spoun.mappers;

import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ScheduleMapper {

    @Mapping(source = "room.id", target = "room_id")
    public ScheduleDTO scheduleToScheduleDTO(Schedule schedule);

    @Mapping(source = "room_id", target = "room.id")
    public Schedule scheduleDTOToSchedule(ScheduleDTO scheduleDTO);

}
