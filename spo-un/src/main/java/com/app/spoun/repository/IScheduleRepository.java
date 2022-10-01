package com.app.spoun.repository;

import com.app.spoun.domain.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IScheduleRepository extends JpaRepository<Schedule, Integer>{

    Page<Schedule> findAll(Pageable page);
    Optional<Schedule> findById(Integer id);
    Schedule save(Schedule schedule);
    void deleteById(Integer id);
    boolean existsById(Integer id);

}