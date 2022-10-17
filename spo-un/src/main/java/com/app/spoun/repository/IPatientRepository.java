package com.app.spoun.repository;

import com.app.spoun.domain.Patient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Long>{
    Page<Patient> findAll(Pageable page);
    Optional<Patient> findById(Long id);
    Optional<Patient> findByUsername(String username);
    Patient save(Patient patient);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByUsername(String username);

}