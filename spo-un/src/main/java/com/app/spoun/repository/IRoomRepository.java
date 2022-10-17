package com.app.spoun.repository;

import com.app.spoun.domain.Room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IRoomRepository extends JpaRepository<Room, Long>{

    Page<Room> findAll(Pageable page);
    Optional<Room> findById(Long id);
    Room save(Room room);
    void deleteById(Long id);
    boolean existsById(Long id);

}