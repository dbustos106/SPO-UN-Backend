package com.app.spoun.repository;

import com.app.spoun.domain.Antecedent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAntecedentRepository extends JpaRepository<Antecedent, Long>{

    Page<Antecedent> findAll(Pageable page);
    Optional<Antecedent> findById(Long id);
    Antecedent save(Antecedent antecedent);
    void deleteById(Long id);
    boolean existsById(Long id);

}