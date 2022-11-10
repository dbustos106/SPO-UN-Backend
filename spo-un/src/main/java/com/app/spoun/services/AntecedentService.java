package com.app.spoun.services;

import com.app.spoun.domain.Antecedent;
import com.app.spoun.domain.Patient;
import com.app.spoun.dto.AntecedentDTO;
import com.app.spoun.mappers.AntecedentMapper;
import com.app.spoun.repository.IAntecedentRepository;
import com.app.spoun.repository.IPatientRepository;

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
public class AntecedentService{

    private final IAntecedentRepository iAntecedentRepository;
    private final IPatientRepository iPatientRepository;
    private final AntecedentMapper antecedentMapper;

    @Autowired
    public AntecedentService(IAntecedentRepository iAntecedentRepository,
                             IPatientRepository iPatientRepository,
                             AntecedentMapper antecedentMapper){
        this.iAntecedentRepository = iAntecedentRepository;
        this.iPatientRepository = iPatientRepository;
        this.antecedentMapper = antecedentMapper;
    }


    public Map<String, Object> getAllAntecedent(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of antecedents
        Pageable page = PageRequest.of(idPage, size);
        Page<Antecedent> antecedents = iAntecedentRepository.findAll(page);

        // map all antecedents
        List<AntecedentDTO> listAntecedentDTOS = new ArrayList<>();
        for(Antecedent antecedent : antecedents){
            AntecedentDTO antecedentDTO = antecedentMapper.antecedentToAntecedentDTO(antecedent);
            listAntecedentDTOS.add(antecedentDTO);
        }
        Page<AntecedentDTO> antecedentDTOS = new PageImpl<>(listAntecedentDTOS);

        // return page of antecedents
        answer.put("message", antecedentDTOS);

        return answer;
    }

    public Map<String, Object> findAntecedentById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Antecedent antecedent = iAntecedentRepository.findById(id).orElse(null);
        if(antecedent != null){
            AntecedentDTO antecedentDTO = antecedentMapper.antecedentToAntecedentDTO(antecedent);
            answer.put("message", antecedentDTO);
        }else{
            throw new NotFoundException("Antecedent not found");
        }
        return answer;
    }

    public Map<String, Object> saveAntecedent(AntecedentDTO antecedentDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(antecedentDTO == null){
            throw new IllegalStateException("Request data missing");
        }else{
            // get patient
            Patient patient = iPatientRepository.findById(antecedentDTO.getPatient_id()).orElse(null);

            // save antecedent
            Antecedent antecedent = antecedentMapper.antecedentDTOToAntecedent(antecedentDTO);
            antecedent.setPatient(patient);
            iAntecedentRepository.save(antecedent);
            answer.put("message", "Antecedent saved successfully");
        }
        return answer;
    }

    public Map<String, Object> editAntecedent(AntecedentDTO antecedentDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(antecedentDTO == null){
            throw new IllegalStateException("Request data missing");
        }else{
            Antecedent antecedent = iAntecedentRepository.findById(antecedentDTO.getId()).orElse(null);
            if(antecedent == null){
                throw new NotFoundException("Antecedent not found");
            }else {
                // update antecedent
                antecedent.setType(antecedentDTO.getType());
                antecedent.setDescription(antecedentDTO.getDescription());

                iAntecedentRepository.save(antecedent);
                answer.put("message", "Antecedent updated successfully");
            }
        }
        return answer;
    }

    public Map<String,Object> deleteAntecedent(Long id){
        Map<String,Object> answer = new TreeMap<>();

        if(iAntecedentRepository.existsById(id)){
            iAntecedentRepository.deleteById(id);
            answer.put("message", "Successful");
        }else{
            throw new NotFoundException("Antecedent not found");
        }
        return answer;
    }

}