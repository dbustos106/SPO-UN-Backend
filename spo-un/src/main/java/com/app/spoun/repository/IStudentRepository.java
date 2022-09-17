package com.app.spoun.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.spoun.models.Student;

import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Integer>{
    Page<Student> findAll(Pageable page);
    Optional<Student> findById(Integer id);
    Student save(Student student);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}