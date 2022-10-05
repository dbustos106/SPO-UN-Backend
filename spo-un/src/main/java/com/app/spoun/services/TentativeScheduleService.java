package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.TentativeSchedule;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.mappers.TentativeScheduleMapper;
import com.app.spoun.mappers.TentativeScheduleMapperImpl;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.ITentativeScheduleRepository;
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
public class TentativeScheduleService {

    @Autowired
    private ITentativeScheduleRepository iTentativeScheduleRepository;

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    private TentativeScheduleMapper tentativeScheduleMapper = new TentativeScheduleMapperImpl();

    public Map<String,Object> getAllTentativeSchedule(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

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
        if(tentativeScheduleDTOS.getSize() != 0){
            answer.put("message", tentativeScheduleDTOS);
        }else {
            answer.put("error", "No tentative schedule found");
        }
        return answer;
    }

    public Map<String,Object> findTentativeScheduleById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        TentativeSchedule tentativeSchedule = iTentativeScheduleRepository.findById(id).orElse(null);
        TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
        if(tentativeScheduleDTO != null){
            answer.put("message", tentativeScheduleDTO);
        }else{
            answer.put("error", "Tentative schedule not found");
        }
        return answer;
    }

    public Map<String,Object> saveTentativeSchedule(TentativeScheduleDTO tentativeScheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(tentativeScheduleDTO != null){
            // get appointment
            Appointment appointment = iAppointmentRepository.findById(tentativeScheduleDTO.getAppointment_id()).orElse(null);

            // save schedule
            TentativeSchedule tentativeSchedule = tentativeScheduleMapper.tentativeScheduleDTOToTentativeSchedule(tentativeScheduleDTO);
            tentativeSchedule.setAppointment(appointment);
            iTentativeScheduleRepository.save(tentativeSchedule);
            answer.put("message", "Tentative schedule saved successfully");
        }else{
            answer.put("error", "Tentative schedule not saved");
        }
        return answer;
    }

    public Map<String,Object> editTentativeSchedule(TentativeScheduleDTO tentativeScheduleDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(tentativeScheduleDTO.getId() != null && iTentativeScheduleRepository.existsById(tentativeScheduleDTO.getId())){
            // get appointment
            Appointment appointment = iAppointmentRepository.findById(tentativeScheduleDTO.getAppointment_id()).orElse(null);

            // update schedule
            TentativeSchedule tentativeSchedule = tentativeScheduleMapper.tentativeScheduleDTOToTentativeSchedule(tentativeScheduleDTO);
            tentativeSchedule.setAppointment(appointment);
            iTentativeScheduleRepository.save(tentativeSchedule);
            answer.put("message", "Tentative schedule updated successfully");
        }else{
            answer.put("error", "Tentative schedule not found");
        }
        return answer;
    }

    public Map<String,Object> deleteTentativeSchedule(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iTentativeScheduleRepository.existsById(id)){
            iTentativeScheduleRepository.deleteById(id);
            answer.put("message", "Tentative schedule deleted successfully");
        }else{
            answer.put("error", "Tentative schedule not found");
        }
        return answer;
    }

}