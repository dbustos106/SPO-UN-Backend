package com.app.spoun.repository;

import com.app.spoun.domain.Schedule;
import com.app.spoun.dto.ScheduleDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Long>{

    Page<Schedule> findAll(Pageable page);
    Optional<Schedule> findById(Long id);
    Schedule save(ScheduleDTO scheduleDTO);
    void deleteById(Long id);
    boolean existsById(Long id);
    List<Schedule> findByRoom_id(Long id);

    @Modifying
    @Query(value = """
            DELETE
            FROM schedule
            WHERE (start_time = ?1);
            """, nativeQuery = true)
    void deleteByStart_time(String start_time);

}