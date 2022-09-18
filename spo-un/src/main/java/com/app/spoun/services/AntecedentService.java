package com.app.spoun.services;

import com.app.spoun.repository.IAntecedentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.app.spoun.models.Antecedent;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;


@Service
public class AntecedentService {
    @Autowired
    private IAntecedentRepository iAntecedentRepository;

    public Map<String,Object> getAllAntecedent (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Antecedent> antecedents = iAntecedentRepository.findAll(page);
        if(antecedents.getSize() != 0){
            answer.put("antecedents", antecedents);
        }else {
            answer.put("error", "No antecedent found");
        }
        return answer;
    }

    public Map<String,Object> findById(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        Antecedent antecedent = iAntecedentRepository.findById(id).orElse(null);
        if(antecedent != null){
            answer.put("antecedent", antecedent);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> saveAntecedent(Antecedent antecedent){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedent != null){
            System.out.println("Guardar antecedent");
            Antecedent antecedent_answer = iAntecedentRepository.save(antecedent);
            answer.put("antecedent", antecedent_answer);
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

    public Map<String,Object> editAntecedent(Antecedent antecedent){
        Map<String,Object> answer = new TreeMap<>();
        if(antecedent.getId() != null && iAntecedentRepository.existsById(antecedent.getId())){
            Antecedent antecedent_answer = iAntecedentRepository.save(antecedent);
            answer.put("antecedent", antecedent_answer);
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Antecedent not found");
        }
        return answer;
    }

    public Map<String,Object> deleteAntecedent(Integer id){
        Map<String,Object> answer = new TreeMap<>();
        if(iAntecedentRepository.existsById(id)){
            iAntecedentRepository.deleteById(id);
            answer.put("menssage", "Successful");
        }else{
            answer.put("error", "Not successful");
            answer.put("message", "Antecedent not found");
        }
        return answer;
    }

    public boolean existById(Integer id){
        return iAntecedentRepository.existsById(id);
    }

}