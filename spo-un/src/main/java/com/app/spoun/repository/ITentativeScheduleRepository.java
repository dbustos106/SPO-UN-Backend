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
public interface ITentativeScheduleRepository extends JpaRepository<TentativeSchedule, Long>{

    Page<TentativeSchedule> findAll(Pageable page);
    Optional<TentativeSchedule> findById(Long id);
    TentativeSchedule save(TentativeSchedule tentativeSchedule);
    void deleteById(Long id);
    boolean existsById(Long id);
    void deleteByAppointment_id(Long id);
    List<TentativeSchedule> findByAppointment_id(Long id);

    @Query(value = "SELECT tentative_schedule.id, tentative_schedule.start_time, tentative_schedule.end_time, tentative_schedule.appointment_id\n" +
            "FROM ((student_appointment INNER JOIN appointment ON student_appointment.appointment_id = appointment.id) INNER JOIN\n" +
            "tentative_schedule ON tentative_schedule.appointment_id = appointment.id)\n" +
            "WHERE student_appointment.student_id = ?1 and appointment.state = 'Available'", nativeQuery = true)
    List<TentativeSchedule> getStudentUnconfirmedScheduleByStudentId(Long id);

}