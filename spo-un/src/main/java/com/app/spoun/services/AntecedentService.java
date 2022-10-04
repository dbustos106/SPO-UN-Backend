package com.app.spoun.services;

import com.app.spoun.domain.Antecedent;
import com.app.spoun.domain.Patient;
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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
public class AntecedentService {

    @Autowired
    private IAntecedentRepository iAntecedentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    private AntecedentMapper antecedentMapper = new AntecedentMapperImpl();

    public Map<String,Object> getAllAntecedent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        // get page of antecedents
        Pageable page = PageRequest.of(idPage, size);
        Page<Antecedent> antecedents = iAntecedentRepository.findAll(page);

        // map all antecedents
        List<AntecedentDTO> listAntecedentsDTO = new ArrayList<>();
        for(Antecedent antecedent : antecedents){
            AntecedentDTO antecedentDTO = antecedentMapper.antecedentToAntecedentDTO(antecedent);
            listAntecedentsDTO.add(antecedentDTO);
        }
        Page<AntecedentDTO> antecedentsDTO = new PageImpl<>(listAntecedentsDTO);

        // return page of antecedents
        if(antecedentsDTO.getSize() != 0){
            answer.put("message", antecedentsDTO);
        }else {
            answer.put("error", "No antecedent found");
        }
        return answer;
    }

    public Map<String,Object> findAntecedentById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Antecedent antecedent = iAntecedentRepository.findById(id).orElse(null);
        AntecedentDTO antecedentDTO = antecedentMapper.antecedentToAntecedentDTO(antecedent);
        if(antecedentDTO != null){
            answer.put("message", antecedentDTO);
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> saveAntecedent(AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedentDTO != null){
            // get patient
            Patient patient = iPatientRepository.findById(antecedentDTO.getPatient_id()).orElse(null);

            // save antecedent
            Antecedent antecedent = antecedentMapper.antecedentDTOToAntecedent(antecedentDTO);
            antecedent.setPatient(patient);
            iAntecedentRepository.save(antecedent);
            answer.put("message", "Antecedent saved successfully");
        }else{
            answer.put("error", "Antecedent not saved");
        }
        return answer;
    }

    public Map<String,Object> editAntecedent(AntecedentDTO antecedentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedentDTO != null && antecedentDTO.getId() != null && iAntecedentRepository.existsById(antecedentDTO.getId())){
            // get patient
            Patient patient = iPatientRepository.findById(antecedentDTO.getPatient_id()).orElse(null);

            // update antecedent
            Antecedent antecedent = antecedentMapper.antecedentDTOToAntecedent(antecedentDTO);
            antecedent.setPatient(patient);
            iAntecedentRepository.save(antecedent);
            answer.put("message", "Antecedent updated successfully");
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAntecedent(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAntecedentRepository.existsById(id)){
            iAntecedentRepository.deleteById(id);
            answer.put("message", "Successful");
        }else{
            answer.put("error", "Antecedent not found");
        }
        return answer;
    }

}