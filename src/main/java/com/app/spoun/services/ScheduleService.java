package com.app.spoun.services;

import com.app.spoun.domain.Room;
import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.mappers.ScheduleMapper;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.IRoomRepository;
import com.app.spoun.repository.IScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Transactional
@Service
public class ScheduleService {

    private final IScheduleRepository iScheduleRepository;
    private final IAppointmentRepository iAppointmentRepository;
    private final IRoomRepository iRoomRepository;
    private final ScheduleMapper scheduleMapper;

    @Autowired
    public ScheduleService(IScheduleRepository iScheduleRepository,
                           IAppointmentRepository iAppointmentRepository,
                           IRoomRepository iRoomRepository,
                           ScheduleMapper scheduleMapper){
        this.iScheduleRepository = iScheduleRepository;
        this.iAppointmentRepository = iAppointmentRepository;
        this.iRoomRepository = iRoomRepository;
        this.scheduleMapper = scheduleMapper;
    }


    public boolean isAvailableSchedule(List<Schedule> schedules, String start_time, String end_time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        for(Schedule schedule : schedules){
            Date start_schedule = new Date(sdf.parse(schedule.getStart_time()).getTime());
            Date end_schedule = new Date(sdf.parse(schedule.getEnd_time()).getTime());
            Date start_tentative = new Date(sdf.parse(start_time).getTime());
            Date end_tentative = new Date(sdf.parse(end_time).getTime());
            if(!((start_tentative.before(start_schedule) && !end_tentative.after(start_schedule)) ||
                    (start_schedule.before(start_tentative) && !end_schedule.after(start_tentative)))){
                return false;
            }
        }
        return true;
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
        answer.put("message", scheduleDTOS);

        return answer;
    }

    public Map<String, Object> findScheduleById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Schedule schedule = iScheduleRepository.findById(id).orElse(null);
        if(schedule == null){
            throw new NotFoundException("Schedule not found");
        }
        ScheduleDTO scheduleDTO = scheduleMapper.scheduleToScheduleDTO(schedule);
        answer.put("message", scheduleDTO);

        return answer;
    }

    public Map<String, Object> saveSchedule(ScheduleDTO scheduleDTO) throws ParseException {
        Map<String, Object> answer = new TreeMap<>();

        if(scheduleDTO == null){
            throw new IllegalStateException("Request data missing");
        }

        List<Schedule> schedules = iScheduleRepository.findAll();
        if(!isAvailableSchedule(schedules, scheduleDTO.getStart_time(), scheduleDTO.getEnd_time())){
            throw new IllegalStateException("Schedule not available");
        }

        // get room
        Room room = iRoomRepository.findById(scheduleDTO.getRoom_id()).orElse(null);

        // save schedule
        Schedule schedule = scheduleMapper.scheduleDTOToSchedule(scheduleDTO);
        schedule.setRoom(room);

        iScheduleRepository.save(schedule);
        answer.put("message", "Schedule saved successfully");

        return answer;
    }

    public Map<String, Object> editSchedule(ScheduleDTO scheduleDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(scheduleDTO == null){
            throw new IllegalStateException("Request data missing");
        }

        Schedule schedule = iScheduleRepository.findById(scheduleDTO.getId()).orElse(null);
        if(schedule == null) {
            throw new NotFoundException("Schedule not found");
        }
        // update schedule
        schedule.setStart_time(scheduleDTO.getStart_time());
        schedule.setEnd_time(scheduleDTO.getEnd_time());

        iScheduleRepository.save(schedule);
        answer.put("message", "Schedule updated successfully");

        return answer;
    }

    public Map<String, Object> deleteSchedule(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Schedule schedule = iScheduleRepository.findById(id).orElse(null);
        if(schedule == null){
            throw new NotFoundException("Schedule not found");
        }

        String start_time = schedule.getStart_time();
        Long room_id = schedule.getRoom().getId();
        if(iAppointmentRepository.findByStart_timeAndRoom_id(start_time, room_id).size() != 0){
            throw new NotFoundException("Schedule cannot be delete");
        }
        iScheduleRepository.deleteById(id);
        answer.put("message", "Schedule deleted successfully");

        return answer;
    }

}