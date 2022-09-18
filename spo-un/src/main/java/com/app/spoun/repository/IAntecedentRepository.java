package com.app.spoun.repository;

import com.app.spoun.dao.AntecedentDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAntecedentRepository extends JpaRepository<AntecedentDAO, Integer> {
    Page<AntecedentDAO> findAll(Pageable page);
    Optional<AntecedentDAO> findById(Integer id);
    AntecedentDAO save(AntecedentDAO antecedentDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}