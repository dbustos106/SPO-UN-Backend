package com.app.spoun.repository;

import com.app.spoun.dao.AppointmentDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAppointmentRepository extends JpaRepository<AppointmentDAO, Integer> {
    Page<AppointmentDAO> findAll(Pageable page);
    Optional<AppointmentDAO> findById(Integer id);
    AppointmentDAO save(AppointmentDAO appointmentDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}