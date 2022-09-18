package com.app.spoun.services;

import com.app.spoun.repository.IPatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Patient;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class PatientService {
    @Autowired
    private IPatientRepository iPatientRepository;

    public Map<String,Object> getAllPatient (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Patient> patients = iPatientRepository.findAll(page);
        if(patients.getSize() != 0){
            answer.put("patients", patients);
        }else {
            answer.put("error", "No patient found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Patient patient = iPatientRepository.findById(id).orElse(null);
        if(patient != null){
            answer.put("patient", patient);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Patient not found");
        }
        return answer;
    }

    public Map<String,Object> savePatient(Patient patient){
        Map<String,Object> answer = new TreeMap<>();
        if(patient != null){
            System.out.println("Guardar patient");
            Patient patient_answer = iPatientRepository.save(patient);
            answer.put("patient", patient_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editPatient(Patient patient){
        Map<String,Object> answer = new TreeMap<>();
        if(patient.getId() != null && iPatientRepository.existsById(patient.getId())){
            Patient patient_answer = iPatientRepository.save(patient);
            answer.put("patient", patient_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Patient not found");
        }
        return answer;
    }

    public Map<String,Object> deletePatient(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iPatientRepository.existsById(id)){
            iPatientRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Patient not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iPatientRepository.existsById(id);
    }

}