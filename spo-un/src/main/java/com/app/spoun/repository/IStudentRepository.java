package com.app.spoun.repository;

import com.app.spoun.dao.StudentDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<StudentDAO, Integer>{
    Page<StudentDAO> findAll(Pageable page);
    Optional<StudentDAO> findById(Integer id);
    StudentDAO save(StudentDAO studentDAO);
    void deleteById(Integer id);
    boolean existsById(Integer id);
}