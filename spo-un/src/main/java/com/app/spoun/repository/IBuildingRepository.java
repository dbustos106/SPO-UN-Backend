package com.app.spoun.repository;

import com.app.spoun.dao.BuildingDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBuildingRepository extends JpaRepository<BuildingDAO, Integer>{
    Page<BuildingDAO> findAll(Pageable page);
    Optional<BuildingDAO> findById(Integer id);
    BuildingDAO save(BuildingDAO building);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}