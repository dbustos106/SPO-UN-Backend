package com.app.spoun.services;

import com.app.spoun.domain.Professor;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.mappers.ProfessorMapper;
import com.app.spoun.mappers.ProfessorMapperImpl;
import com.app.spoun.repository.IProfessorRepository;
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
public class ProfessorService {

    @Autowired
    private IProfessorRepository iProfessorRepository;

    private ProfessorMapper professorMapper = new ProfessorMapperImpl();

    public Map<String,Object> getAllProfessor(Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Professor> professorsDAO = iProfessorRepository.findAll(page);

        List<ProfessorDTO> listProfessorsDTO = new ArrayList<>();
        for(Professor professor : professorsDAO){
            ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
            listProfessorsDTO.add(professorDTO);
        }
        Page<ProfessorDTO> professorsDTO = new PageImpl<>(listProfessorsDTO);

        if(professorsDTO.getSize() != 0){
            answer.put("professors", professorsDTO);
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
            answer.put("professor", professorDTO);
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

    public Map<String,Object> saveProfessor(ProfessorDTO professorDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(professorDTO != null){
            Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
            iProfessorRepository.save(professor);
            answer.put("message", "Professor saved successfully");
        }else{
            answer.put("error", "Not successful");
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
            answer.put("menssage", "Professor deleted successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }
}

