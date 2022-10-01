package com.app.spoun.repository;

import com.app.spoun.domain.Professor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfessorRepository extends JpaRepository<Professor, Integer> {
    Page<Professor> findAll(Pageable page);
    Optional<Professor> findById(Integer id);
    Optional<Professor> findByUsername(String username);
    Professor save(Professor professor);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    boolean existsByUsername(String username);

}