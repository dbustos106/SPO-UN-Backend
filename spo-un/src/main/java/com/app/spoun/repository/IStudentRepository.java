package com.app.spoun.repository;

import com.app.spoun.domain.Student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IStudentRepository extends JpaRepository<Student, Long>{

    Page<Student> findAll(Pageable page);

    Optional<Student> findById(Long id);

    Optional<Student> findByUsername(String username);

    Student save(Student student);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByUsername(String username);

    Page<Student> findByProfessor_id(Long id, Pageable page);

    @Query(value = "SELECT id, username, password, name, document_type, document_number, professor_id, role_id\n" +
            "FROM (student INNER JOIN student_appointment ON student.id = student_appointment.student_id)\n" +
            "WHERE student_appointment.appointment_id = ?1", nativeQuery = true)
    List<Student> findByAppointment_id(Long id);

}