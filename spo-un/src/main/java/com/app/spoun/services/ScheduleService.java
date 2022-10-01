package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.mappers.ScheduleMapper;
import com.app.spoun.mappers.ScheduleMapperImpl;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.IScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class ScheduleService {

    @Autowired
    private IScheduleRepository iScheduleRepository;

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    private ScheduleMapper scheduleMapper = new ScheduleMapperImpl();

    public Map<String,Object> getAllSchedule (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Schedule> schedules = iScheduleRepository.findAll(page);

        List<ScheduleDTO> listSchedulesDTO = new ArrayList<>();
        for(Schedule schedule : schedules){
            ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
            listSchedulesDTO.add(scheduleDTO);
        }
        Page<ScheduleDTO> schedulesDTO = new PageImpl<>(listSchedulesDTO);

        if(schedulesDTO.getSize() != 0){
            answer.put("message", schedulesDTO);
        }else {
            answer.put("error", "None schedule found");
        }
        return answer;
    }

    public Map<String,Object> findScheduleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Schedule schedule = iScheduleRepository.findById(id).orElse(null);
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
        if(scheduleDTO != null){
            answer.put("message", scheduleDTO);
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

    public Map<String,Object> saveSchedule(ScheduleDTO scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(scheduleDTO != null){
            Appointment appointment = iAppointmentRepository.findById(scheduleDTO.getAppointment_id()).orElse(null);
            Schedule schedule = scheduleMapper.scheduleDTOToSchedule(scheduleDTO);
            schedule.setAppointment(appointment);
            iScheduleRepository.save(schedule);
            answer.put("message", "Schedule saved successfully");
        }else{
            answer.put("error", "Schedule not saved");
        }
        return answer;
    }

    public Map<String,Object> editSchedule(ScheduleDTO scheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(scheduleDTO.getId() != null && iScheduleRepository.existsById(scheduleDTO.getId())){
            Appointment appointment = iAppointmentRepository.findById(scheduleDTO.getAppointment_id()).orElse(null);
            Schedule schedule = scheduleMapper.scheduleDTOToSchedule(scheduleDTO);
            schedule.setAppointment(appointment);
            iScheduleRepository.save(schedule);
            answer.put("message", "Schedule updated successfully");
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

    public Map<String,Object> deleteSchedule(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iScheduleRepository.existsById(id)){
            iScheduleRepository.deleteById(id);
            answer.put("message", "Schedule deleted successfully");
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

}