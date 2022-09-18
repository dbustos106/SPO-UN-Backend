package com.app.spoun.repository;

import com.app.spoun.dao.ProfessorDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfessorRepository extends JpaRepository<ProfessorDAO, Integer> {
    Page<ProfessorDAO> findAll(Pageable page);
    Optional<ProfessorDAO> findById(Integer id);
    ProfessorDAO save(ProfessorDAO professorDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}