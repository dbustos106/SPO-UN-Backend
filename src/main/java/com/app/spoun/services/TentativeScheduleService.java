package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.TentativeSchedule;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.mappers.TentativeScheduleMapper;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.ITentativeScheduleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class TentativeScheduleService {

    private final ITentativeScheduleRepository iTentativeScheduleRepository;
    private final IAppointmentRepository iAppointmentRepository;
    private final TentativeScheduleMapper tentativeScheduleMapper;

    @Autowired
    public TentativeScheduleService(ITentativeScheduleRepository iTentativeScheduleRepository,
                                    IAppointmentRepository iAppointmentRepository,
                                    TentativeScheduleMapper tentativeScheduleMapper){
        this.iTentativeScheduleRepository = iTentativeScheduleRepository;
        this.iAppointmentRepository = iAppointmentRepository;
        this.tentativeScheduleMapper = tentativeScheduleMapper;
    }


    public Map<String, Object> getAllTentativeSchedule(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of tentative schedules
        Pageable page = PageRequest.of(idPage, size);
        Page<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.findAll(page);

        // map all tentative schedules
        List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
        for(TentativeSchedule tentativeSchedule : tentativeSchedules){
            TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
            listTentativeScheduleDTOS.add(tentativeScheduleDTO);
        }
        Page<TentativeScheduleDTO> tentativeScheduleDTOS = new PageImpl<>(listTentativeScheduleDTOS);

        // return page of tentative schedules
        answer.put("message", tentativeScheduleDTOS);

        return answer;
    }

    public Map<String, Object> findTentativeScheduleById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        TentativeSchedule tentativeSchedule = iTentativeScheduleRepository.findById(id).orElse(null);
        if(tentativeSchedule == null){
            throw new NotFoundException("Tentative schedule not found");
        }
        TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
        answer.put("message", tentativeScheduleDTO);

        return answer;
    }

    public Map<String, Object> saveTentativeSchedule(TentativeScheduleDTO tentativeScheduleDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(tentativeScheduleDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        // get appointment
        Appointment appointment = iAppointmentRepository.findById(tentativeScheduleDTO.getAppointment_id()).orElse(null);

        // save schedule
        TentativeSchedule tentativeSchedule = tentativeScheduleMapper.tentativeScheduleDTOToTentativeSchedule(tentativeScheduleDTO);
        tentativeSchedule.setAppointment(appointment);

        iTentativeScheduleRepository.save(tentativeSchedule);
        answer.put("message", "Tentative schedule saved successfully");

        return answer;
    }

    public Map<String, Object> editTentativeSchedule(TentativeScheduleDTO tentativeScheduleDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(tentativeScheduleDTO == null) {
            throw new IllegalStateException("Request data missing");
        }

        TentativeSchedule tentativeSchedule = iTentativeScheduleRepository.findById(tentativeScheduleDTO.getId()).orElse(null);
        if(tentativeSchedule == null) {
            throw new NotFoundException("Tentative schedule not found");
        }
        // update schedule
        tentativeSchedule.setStart_time(tentativeScheduleDTO.getStart_time());
        tentativeSchedule.setEnd_time(tentativeScheduleDTO.getEnd_time());

        iTentativeScheduleRepository.save(tentativeSchedule);
        answer.put("message", "Tentative schedule updated successfully");

        return answer;
    }

    public Map<String, Object> deleteTentativeSchedule(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(!iTentativeScheduleRepository.existsById(id)){
            throw new NotFoundException("Tentative schedule not found");
        }
        iTentativeScheduleRepository.deleteById(id);
        answer.put("message", "Tentative schedule deleted successfully");

        return answer;
    }

}