package com.app.spoun.repository;

import com.app.spoun.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<Patient, Integer> {
    Page<Patient> findAll(Pageable page);
    Optional<Patient> findById(Integer id);
    Patient save(Patient patient);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}