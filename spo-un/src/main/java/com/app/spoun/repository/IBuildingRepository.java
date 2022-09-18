package com.app.spoun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.spoun.models.Building;

import java.util.Optional;

@Repository
public interface IBuildingRepository extends JpaRepository<Building, Integer>{
    Page<Building> findAll(Pageable page);
    Optional<Building> findById(Integer id);
    Building save(Building building);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}