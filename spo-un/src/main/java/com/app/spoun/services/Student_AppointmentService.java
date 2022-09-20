package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.Student;
import com.app.spoun.domain.Student_Appointment;
import com.app.spoun.dto.Student_AppointmentDTO;
import com.app.spoun.mappers.Student_AppointmentMapper;
import com.app.spoun.mappers.Student_AppointmentMapperImpl;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.IStudentRepository;
import com.app.spoun.repository.IStudent_AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class Student_AppointmentService {

    @Autowired
    private IStudent_AppointmentRepository iStudent_AppointmentRepository;

    @Autowired
    private IStudentRepository iStudentRepository;

    @Autowired
    private IAppointmentRepository iAppointmentRepository;

    private Student_AppointmentMapper student_AppointmentMapper = new Student_AppointmentMapperImpl();

    public Map<String,Object> getAllStudent_Appointment (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Student_Appointment> student_Appointments = iStudent_AppointmentRepository.findAll(page);

        List<Student_AppointmentDTO> listStudent_AppointmentsDTO = new ArrayList<>();
        for(Student_Appointment student_Appointment: student_Appointments){
            Student_AppointmentDTO student_AppointmentDTO = student_AppointmentMapper.student_AppointmentToStudent_AppointmentDTO(student_Appointment);
            listStudent_AppointmentsDTO.add(student_AppointmentDTO);
        }
        Page<Student_AppointmentDTO> student_AppointmentsDTO = new PageImpl<>(listStudent_AppointmentsDTO);

        if(student_AppointmentsDTO.getSize() != 0){
            answer.put("student_Appointments", student_AppointmentsDTO);
        }else {
            answer.put("error", "None student_Appointment found");
        }
        return answer;
    }

    public Map<String,Object> saveStudent_Appointment(Student_AppointmentDTO student_AppointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(student_AppointmentDTO != null){
            Student student = iStudentRepository.findById(4).orElse(null);
            Appointment appointment = iAppointmentRepository.findById(student_AppointmentDTO.getAppointment_id()).orElse(null);

            Student_Appointment student_Appointment = student_AppointmentMapper.student_AppointmentDTOToStudent_Appointment(student_AppointmentDTO);
            student_Appointment.setStudent(student);
            student_Appointment.setAppointment(appointment);

            System.out.println("Este es studentAppointmentDAO");
            System.out.println(student_Appointment);

            iStudent_AppointmentRepository.save(student_Appointment);
            answer.put("message", "Student_Appointment saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

}