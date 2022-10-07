package com.app.spoun.repository;

import com.app.spoun.domain.TentativeSchedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ITentativeScheduleRepository extends JpaRepository<TentativeSchedule, Integer>{

    Page<TentativeSchedule> findAll(Pageable page);
    Optional<TentativeSchedule> findById(Integer id);
    TentativeSchedule save(TentativeSchedule tentativeSchedule);
    void deleteById(Integer id);
    boolean existsById(Integer id);

    void deleteByAppointment_id(Integer id);

    List<TentativeSchedule> findByAppointment_id(Integer id);

    @Query(value = "SELECT tentative_schedule.id, tentative_schedule.start_time, tentative_schedule.end_time, tentative_schedule.appointment_id\n" +
            "FROM ((student_appointment INNER JOIN appointment ON student_appointment.appointment_id = appointment.id) INNER JOIN\n" +
            "tentative_schedule ON tentative_schedule.appointment_id = appointment.id)\n" +
            "WHERE student_appointment.student_id = ?1 and appointment.start_time IS NULL and appointment.end_time IS NULL", nativeQuery = true)
    List<TentativeSchedule> getStudentUnconfirmedScheduleByStudentId(Integer id);

}