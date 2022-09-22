package com.app.spoun.repository;

import com.app.spoun.domain.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Integer>{
    Page<Student> findAll(Pageable page);
    Optional<Student> findById(Integer id);
    Optional<Student> findByUsername(String username);
    Student save(Student student);
    void deleteById(Integer id);
    boolean existsById(Integer id);
    boolean existsByUsername(String username);
}