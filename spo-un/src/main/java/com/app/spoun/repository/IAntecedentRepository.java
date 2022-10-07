package com.app.spoun.repository;

import com.app.spoun.domain.Antecedent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAntecedentRepository extends JpaRepository<Antecedent, Integer>{
    Page<Antecedent> findAll(Pageable page);
    Optional<Antecedent> findById(Integer id);
    Antecedent save(Antecedent antecedent);
    void deleteById(Integer id);
    boolean existsById(Integer id);

}