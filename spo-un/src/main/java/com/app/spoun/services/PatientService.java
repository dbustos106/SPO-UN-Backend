package com.app.spoun.services;

import com.app.spoun.domain.Patient;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.mappers.PatientMapper;
import com.app.spoun.mappers.PatientMapperImpl;
import com.app.spoun.repository.IPatientRepository;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
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
public class PatientService {
    @Autowired
    private IPatientRepository iPatientRepository;

    private PatientMapper patientMapper = new PatientMapperImpl();

    public Map<String,Object> getAllPatient (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Patient> patientsDAO = iPatientRepository.findAll(page);

        List<PatientDTO> listPatientsDTO = new ArrayList<>();
        for(Patient patient : patientsDAO){
            PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
            listPatientsDTO.add(patientDTO);
        }
        Page<PatientDTO> patientsDTO = new PageImpl<>(listPatientsDTO);

        if(patientsDTO.getSize() != 0){
            answer.put("patients", patientsDTO);
        }else {
            answer.put("error", "None patient found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Patient patient = iPatientRepository.findById(id).orElse(null);
        PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
        if(patientDTO != null){
            answer.put("patient", patientDTO);
        }else{
            answer.put("error", "Patient not found");
        }
        return answer;
    }

    public Map<String,Object> savePatient(PatientDTO patientDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(patientDTO != null){
            Patient patient = patientMapper.patientDTOToPatient(patientDTO);

            // encrypt password
            Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
            String hashPasword = argon2.hash(1, 1024, 1, patient.getPassword());
            //patient.setPassword(hashPasword);

            iPatientRepository.save(patient);
            answer.put("patient", "Patient saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editPatient(PatientDTO patientDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(patientDTO.getId() != null && iPatientRepository.existsById(patientDTO.getId())){
            Patient patient = patientMapper.patientDTOToPatient(patientDTO);
            iPatientRepository.save(patient);
            answer.put("patient", "Patient updated successfully");
        }else{
            answer.put("error", "Patient not found");
        }
        return answer;
    }

    public Map<String,Object> deletePatient(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iPatientRepository.existsById(id)){
            iPatientRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Patient not found");
        }
        return answer;
    }

}