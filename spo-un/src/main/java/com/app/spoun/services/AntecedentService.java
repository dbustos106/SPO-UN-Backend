package com.app.spoun.services;

import com.app.spoun.dao.AntecedentDAO;
import com.app.spoun.dao.PatientDAO;
import com.app.spoun.dto.AntecedentDTO;
import com.app.spoun.mappers.AntecedentMapper;
import com.app.spoun.mappers.AntecedentMapperImpl;
import com.app.spoun.repository.IAntecedentRepository;
import com.app.spoun.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class AntecedentService {
    @Autowired
    private IAntecedentRepository iAntecedentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    private AntecedentMapper antecedentMapper = new AntecedentMapperImpl();

    public Map<String,Object> getAllAntecedent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<AntecedentDAO> antecedentsDAO = iAntecedentRepository.findAll(page);

        List<AntecedentDTO> listAntecedentsDTO = new ArrayList<>();
        for(AntecedentDAO antecedentDAO: antecedentsDAO){
            AntecedentDTO antecedentDTO = antecedentMapper.antecedentDAOToAntecedentDTO(antecedentDAO);
            listAntecedentsDTO.add(antecedentDTO);
        }
        Page<AntecedentDTO> antecedentsDTO = new PageImpl<>(listAntecedentsDTO);

        if(antecedentsDTO.getSize() != 0){
            answer.put("antecedents", antecedentsDTO);
        }else {
            answer.put("error", "None antecedent found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        AntecedentDAO antecedentDAO = iAntecedentRepository.findById(id).orElse(null);
        AntecedentDTO antecedentDTO = antecedentMapper.antecedentDAOToAntecedentDTO(antecedentDAO);
        if(antecedentDTO != null){
            answer.put("antecedent", antecedentDTO);
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> saveAntecedent(AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedentDTO != null){

            PatientDAO patientDAO = iPatientRepository.findById(antecedentDTO.getPatient_id()).orElse(null);
            AntecedentDAO antecedentDAO = antecedentMapper.antecedentDTOToAntecedentDAO(antecedentDTO);
            antecedentDAO.setPatient(patientDAO);
            iAntecedentRepository.save(antecedentDAO);
            answer.put("antecedent", "Antecedent saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editAntecedent(AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedentDTO.getId() != null && iAntecedentRepository.existsById(antecedentDTO.getId())){
            PatientDAO patientDAO = iPatientRepository.findById(antecedentDTO.getPatient_id()).orElse(null);
            AntecedentDAO antecedentDAO = antecedentMapper.antecedentDTOToAntecedentDAO(antecedentDTO);
            antecedentDAO.setPatient(patientDAO);
            iAntecedentRepository.save(antecedentDAO);
            answer.put("antecedent", "Student updated successfully");
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAntecedent(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAntecedentRepository.existsById(id)){
            iAntecedentRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

}