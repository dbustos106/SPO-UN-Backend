package com.app.spoun.repository;

import com.app.spoun.dao.PatientDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IPatientRepository extends JpaRepository<PatientDAO, Integer> {
    Page<PatientDAO> findAll(Pageable page);
    Optional<PatientDAO> findById(Integer id);
    PatientDAO save(PatientDAO patientDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}