package com.app.spoun.repository;

import com.app.spoun.dao.Student_AppointmentPK;
import com.app.spoun.dao.Student_AppointmentDAO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudent_AppointmentRepository extends JpaRepository<Student_AppointmentDAO, Student_AppointmentPK> {

    Page<Student_AppointmentDAO> findAll(Pageable page);

    Student_AppointmentDAO save(Student_AppointmentDAO studentDAO);

}
