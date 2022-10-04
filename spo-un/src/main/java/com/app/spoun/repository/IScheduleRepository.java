package com.app.spoun.repository;

import com.app.spoun.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer>{

    Page<Schedule> findAll(Pageable page);
    Optional<Schedule> findById(Integer id);
    Schedule save(Schedule schedule);
    void deleteById(Integer id);
    boolean existsById(Integer id);

    void deleteByAppointment_id(Integer id);

    List<Schedule> findByAppointment_id(Integer id);

    @Query(value = "SELECT schedule.id, schedule.start_time, schedule.end_time, schedule.appointment_id\n" +
            "FROM ((student_appointment INNER JOIN appointment ON student_appointment.appointment_id = appointment.id) INNER JOIN\n" +
            "schedule ON schedule.appointment_id = appointment.id)\n" +
            "WHERE student_appointment.student_id = ?1 and appointment.start_time IS NULL and appointment.end_time IS Null", nativeQuery = true)
    List<Schedule> getUnconfirmedStudentScheduleById(Integer id);

}