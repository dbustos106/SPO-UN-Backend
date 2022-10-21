package com.app.spoun.services;

import com.app.spoun.domain.Room;
import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.mappers.ScheduleMapper;
import com.app.spoun.repository.IRoomRepository;
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

    private IScheduleRepository iScheduleRepository;
    private IRoomRepository iRoomRepository;
    private ScheduleMapper scheduleMapper;

    @Autowired
    public ScheduleService(IScheduleRepository iScheduleRepository,
                           IRoomRepository iRoomRepository,
                           ScheduleMapper scheduleMapper){
        this.iScheduleRepository = iScheduleRepository;
        this.iRoomRepository = iRoomRepository;
        this.scheduleMapper = scheduleMapper;
    }


    public Map<String, Object> getAllSchedule(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of schedules
        Pageable page = PageRequest.of(idPage, size);
        Page<Schedule> schedules = iScheduleRepository.findAll(page);

        // map all schedules
        List<ScheduleDTO> listScheduleDTOS = new ArrayList<>();
        for(Schedule schedule : schedules){
            ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
            listScheduleDTOS.add(scheduleDTO);
        }
        Page<ScheduleDTO> scheduleDTOS = new PageImpl<>(listScheduleDTOS);

        // return page of schedules
        if(scheduleDTOS.getSize() != 0){
            answer.put("message", scheduleDTOS);
        }else {
            answer.put("error", "No schedule found");
        }
        return answer;
    }

    public Map<String, Object> findScheduleById(Long id){
        Map<String, Object> answer = new TreeMap<>();
        Schedule schedule = iScheduleRepository.findById(id).orElse(null);
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
        if(scheduleDTO != null){
            answer.put("message", scheduleDTO);
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

    public Map<String, Object> saveSchedule(ScheduleDTO scheduleDTO){
        Map<String, Object> answer = new TreeMap<>();
        if(scheduleDTO != null){
            // get room
            Room room = iRoomRepository.findById(scheduleDTO.getRoom_id()).orElse(null);

            // save schedule
            Schedule schedule = scheduleMapper.scheduleDTOToSchedule(scheduleDTO);
            schedule.setRoom(room);
            iScheduleRepository.save(schedule);
            answer.put("message", "Schedule saved successfully");
        }else{
            answer.put("error", "Schedule not saved");
        }
        return answer;
    }

    public Map<String, Object> editSchedule(ScheduleDTO scheduleDTO){
        Map<String, Object> answer = new TreeMap<>();
        if(scheduleDTO.getId() != null && iScheduleRepository.existsById(scheduleDTO.getId())){
            // get room
            Room room = iRoomRepository.findById(scheduleDTO.getRoom_id()).orElse(null);

            // update schedule
            Schedule schedule = scheduleMapper.scheduleDTOToSchedule(scheduleDTO);
            schedule.setRoom(room);
            iScheduleRepository.save(schedule);
            answer.put("message", "Schedule updated successfully");
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

    public Map<String, Object> deleteSchedule(Long id){
        Map<String, Object> answer = new TreeMap<>();
        if(iScheduleRepository.existsById(id)){
            iScheduleRepository.deleteById(id);
            answer.put("message", "Schedule deleted successfully");
        }else{
            answer.put("error", "Schedule not found");
        }
        return answer;
    }

}