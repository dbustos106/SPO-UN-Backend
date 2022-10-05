package com.app.spoun.mappers;

import com.app.spoun.domain.TentativeSchedule;
import com.app.spoun.dto.TentativeScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface TentativeScheduleMapper {

    @Mapping(source = "appointment.id", target = "appointment_id")
    public TentativeScheduleDTO tentativeScheduleToTentativeScheduleDTO(TentativeSchedule tentativeSchedule);

    @Mapping(source = "appointment_id", target = "appointment.id")
    public TentativeSchedule tentativeScheduleDTOToTentativeSchedule(TentativeScheduleDTO tentativeScheduleDTO);

}
