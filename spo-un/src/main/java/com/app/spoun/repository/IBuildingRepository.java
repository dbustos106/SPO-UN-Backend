package com.app.spoun.repository;

import com.app.spoun.domain.Building;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBuildingRepository extends JpaRepository<Building, Long>{

    Page<Building> findAll(Pageable page);
    Optional<Building> findById(Long id);
    Building save(Building building);
    void deleteById(Long id);
    boolean existsById(Long id);

}