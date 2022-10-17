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
public interface IAppointmentRepository extends JpaRepository<Appointment, Long>{
    Page<Appointment> findAll(Pageable page);
    Optional<Appointment> findById(Long id);
    Appointment save(Appointment appointment);
    void deleteById(Long id);
    boolean existsById(Long id);

    @Query(value = "SELECT *\n" +
            "FROM appointment\n" +
            "WHERE start_time IS NULL and end_time IS NULL", nativeQuery = true)
    Page<Appointment> findAvailable(Pageable page);

    @Query(value = "SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id\n" +
            "FROM (appointment INNER JOIN student_appointment ON appointment.id = student_appointment.appointment_id)\n" +
            "WHERE student_appointment.student_id = ?1", nativeQuery = true)
    Page<Appointment> findByStudentId(Long id, Pageable page);

    @Query(value = "SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id\n" +
            "FROM (appointment INNER JOIN student_appointment ON appointment.id = student_appointment.appointment_id)\n" +
            "WHERE student_appointment.student_id = ?1 and appointment.start_time IS NOT NULL and appointment.end_time IS NOT NULL", nativeQuery = true)
    List<Appointment> getStudentScheduleByStudentId(Long id);

    @Query(value = "SELECT appointment.id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id\n" +
            "FROM (appointment INNER JOIN professor ON professor.id = appointment.professor_id)\n" +
            "WHERE professor.id = ?1 and appointment.start_time IS NOT NULL and appointment.end_time IS NOT NULL", nativeQuery = true)
    List<Appointment> getProfessorScheduleByProfessorId(Long id);

    @Query(value = "SELECT appointment.id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id\n" +
            "FROM (appointment INNER JOIN patient ON patient.id = appointment.patient_id)\n" +
            "WHERE patient.id = ?1 and appointment.start_time IS NOT NULL and appointment.end_time IS NOT NULL", nativeQuery = true)
    List<Appointment> getPatientScheduleByPatientId(Long id);

}