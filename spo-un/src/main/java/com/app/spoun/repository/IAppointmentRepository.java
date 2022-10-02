package com.app.spoun.repository;

import com.app.spoun.domain.Appointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IAppointmentRepository extends JpaRepository<Appointment, Integer> {
    Page<Appointment> findAll(Pageable page);
    Optional<Appointment> findById(Integer id);
    Appointment save(Appointment appointment);
    void deleteById(Integer id);
    boolean existsById(Integer id);

    @Query(value = "SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id\n" +
            "FROM (appointment INNER JOIN student_appointment ON appointment.id = student_appointment.appointment_id)\n" +
            "WHERE student_appointment.student_id = ?1 and appointment.start_time IS NOT NULL and appointment.end_time IS NOT NULL;", nativeQuery = true)
    List<Appointment> getStudentConfirmedScheduleById(Integer id);

}