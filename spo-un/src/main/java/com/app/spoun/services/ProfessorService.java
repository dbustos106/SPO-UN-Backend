package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.mappers.ProfessorMapper;
import com.app.spoun.mappers.ProfessorMapperImpl;
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
public class ProfessorService {

    @Autowired
    private IProfessorRepository iProfessorRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IPatientRepository iPatientRepository;

    @Autowired
    private IAdminRepository iAdminRepository;

    @Autowired
    private IRoleRepository iRoleRepository;

    private ProfessorMapper professorMapper = new ProfessorMapperImpl();

    private final PasswordEncoder passwordEncoder;

    public Map<String,Object> getAllProfessor(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Professor> professors = iProfessorRepository.findAll(page);

        List<ProfessorDTO> listProfessorsDTO = new ArrayList<>();
        for(Professor professor : professors){
            ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
            listProfessorsDTO.add(professorDTO);
        }
        Page<ProfessorDTO> professorsDTO = new PageImpl<>(listProfessorsDTO);

        if(professorsDTO.getSize() != 0){
            answer.put("message", professorsDTO);
        }else {
            answer.put("error", "None professor found");
        }
        return answer;
    }

    public Map<String,Object> findProfessorById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Professor professor = iProfessorRepository.findById(id).orElse(null);
        ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
        if(professorDTO != null){
            answer.put("message", professorDTO);
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

    public Map<String,Object> saveProfessor(ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(professorDTO != null){
            if(iStudentRepository.existsByUsername(professorDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(professorDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(professorDTO.getUsername())){
                answer.put("error", "Repeated username");
            }else {
                Role role = iRoleRepository.findByName("Professor").orElse(null);
                Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
                professor.setRole(role);

                // encrypt password
                professor.setPassword(passwordEncoder.encode(professor.getPassword()));

                Professor professor_answer = iProfessorRepository.save(professor);
                answer.put("message", "Professor " + professor_answer.getId() + " saved successfully");
            }
        }else{
            answer.put("error", "Professor not saved");
        }
        return answer;
    }

    public Map<String,Object> editProfessor(ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(professorDTO.getId() != null && iProfessorRepository.existsById(professorDTO.getId())){
            Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
            iProfessorRepository.save(professor);
            answer.put("message", "Professor updated successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

    public Map<String,Object> deleteProfessor(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iProfessorRepository.existsById(id)){
            iProfessorRepository.deleteById(id);
            answer.put("message", "Professor deleted successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }
}

