package com.app.spoun.repository;

import com.app.spoun.domain.Student_Appointment;
import com.app.spoun.domain.Student_Appointment_PK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IStudent_AppointmentRepository extends JpaRepository<Student_Appointment, Student_Appointment_PK> {

    Page<Student_Appointment> findAll(Pageable page);

    Student_Appointment save(Student_Appointment student_Appointment);

}
