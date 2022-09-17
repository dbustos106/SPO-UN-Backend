package com.app.spoun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.spoun.models.Room;

import java.util.Optional;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Integer>{
    Page<Room> findAll(Pageable page);
    Optional<Room> findById(Integer id);
    Room save(Room room);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}