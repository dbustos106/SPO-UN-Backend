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

    @Query(value = """
            SELECT *
            FROM (appointment)
            WHERE (appointment.state = 'Available')""", nativeQuery = true)
    List<Appointment> findAllAvailable();

    @Query(value = """
            SELECT *
            FROM (appointment)
            WHERE (appointment.start_time = ?1 and appointment.room_id = ?2)""", nativeQuery = true)
    List<Appointment> findByStart_timeAndRoom_id(String start_time, Long room_id);

    @Query(value = """
            SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment INNER JOIN student_appointment ON appointment.id = student_appointment.appointment_id)
            WHERE (student_appointment.student_id = ?1 and appointment.state <> 'Canceled')""", nativeQuery = true)
    Page<Appointment> findByStudentId(Long id, Pageable page);

    @Query(value = """
            SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment)
            WHERE (appointment.patient_id = ?1 and appointment.state = 'Confirmed')""", nativeQuery = true)
    Page<Appointment> findByPatientId(Long id, Pageable page);

    @Query(value = """
            SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment)
            WHERE (appointment.professor_id = ?1 and appointment.state <> 'Canceled')""", nativeQuery = true)
    Page<Appointment> findByProfessorId(Long id, Pageable page);

    @Query(value = """
            SELECT id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment INNER JOIN student_appointment ON appointment.id = student_appointment.appointment_id)
            WHERE (student_appointment.student_id = ?1 and appointment.state = 'Confirmed')""", nativeQuery = true)
    List<Appointment> getStudentScheduleByStudentId(Long id);

    @Query(value = """
            SELECT appointment.id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment INNER JOIN professor ON professor.id = appointment.professor_id)
            WHERE (professor.id = ?1 and appointment.state = 'Confirmed')""", nativeQuery = true)
    List<Appointment> getProfessorScheduleByProfessorId(Long id);

    @Query(value = """
            SELECT appointment.id, start_time, end_time, procedure_type, state, cancel_reason, patient_rating, patient_feedback, room_id, patient_id, professor_id
            FROM (appointment INNER JOIN patient ON patient.id = appointment.patient_id)
            WHERE (patient.id = ?1 and appointment.state = 'Confirmed')""", nativeQuery = true)
    List<Appointment> getPatientScheduleByPatientId(Long id);

}