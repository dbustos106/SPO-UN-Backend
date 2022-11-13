package com.app.spoun.repository;

import com.app.spoun.domain.Professor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProfessorRepository extends JpaRepository<Professor, Long>{

    Page<Professor> findAll(Pageable page);
    Optional<Professor> findById(Long id);
    Optional<Professor> findByEmail(String email);
    Professor save(Professor professor);
    void deleteById(Long id);
    boolean existsById(Long id);
    boolean existsByEmail(String email);

    @Query(value = """
            SELECT *
            FROM (professor)
            WHERE (professor.verification_code = ?1)""", nativeQuery = true)
    Optional<Professor> findByVerification_code(String code);

}