package com.app.spoun.services;

import com.app.spoun.domain.Patient;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.mappers.PatientMapper;
import com.app.spoun.mappers.PatientMapperImpl;
import com.app.spoun.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class PatientService {
    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    private PatientMapper patientMapper = new PatientMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String,Object> addRoleToPatient(String username, String roleName){
        Map<String,Object> answer = new TreeMap<>();

        Patient patient = iPatientRepository.findByUsername(username).orElse(null);
        Role role = iRoleRepository.findByName(roleName).orElse(null);

        if(patient != null && role != null) {
            patient.getRoles().add(role);
            answer.put("message", "Role added successfully");
        }else{
            answer.put("error", "Not successful");
        }

        return answer;
    }

    public Map<String,Object> getAllPatient (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Patient> patients = iPatientRepository.findAll(page);

        List<PatientDTO> listPatientsDTO = new ArrayList<>();
        for(Patient patient : patients){
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
            if(iProfessorRepository.existsByUsername(patientDTO.getUsername()) ||
                    iStudentRepository.existsByUsername(patientDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(patientDTO.getUsername())){
                answer.put("error", "Repeated username");
            }else {
                Patient patient = patientMapper.patientDTOToPatient(patientDTO);
                patient.setRoles(new ArrayList<>());

                // encrypt password
                patient.setPassword(passwordEncoder.encode(patient.getPassword()));

                iPatientRepository.save(patient);
                answer.put("patient", "Patient saved successfully");
            }
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