package com.app.spoun.repository;

import com.app.spoun.dao.RoomDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoomRepository extends JpaRepository<RoomDAO, Integer>{
    Page<RoomDAO> findAll(Pageable page);
    Optional<RoomDAO> findById(Integer id);
    RoomDAO save(RoomDAO roomDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}