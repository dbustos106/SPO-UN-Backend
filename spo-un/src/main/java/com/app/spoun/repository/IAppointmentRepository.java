package com.app.spoun.repository;

import com.app.spoun.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findAll(Pageable page);
    Optional<Appointment> findById(Integer id);
    Appointment save(Appointment appointment);
    void deleteById(Integer id);
    boolean existsById(Integer id);

}