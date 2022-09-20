package com.app.spoun.services;

import com.app.spoun.dao.AppointmentDAO;
import com.app.spoun.dao.StudentDAO;
import com.app.spoun.dao.Student_AppointmentDAO;
import com.app.spoun.dto.Student_AppointmentDTO;
import com.app.spoun.mappers.StudentMapperImpl;
import com.app.spoun.mappers.Student_AppointmentMapper;
import com.app.spoun.mappers.Student_AppointmentMapperImpl;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.IStudentRepository;
import com.app.spoun.repository.IStudent_AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    /*
    public Map<String,Object> getAllStudent_Appointment (Integer idPage, Integer size){
        Map<String,Object> answer = new TreeMap<>();

        Pageable page = PageRequest.of(idPage, size);
        Page<Student_AppointmentDAO> student_AppointmentsDAO = iStudent_AppointmentRepository.findAll(page);

        List<Student_AppointmentDTO> listStudent_AppointmentsDTO = new ArrayList<>();
        for(Student_AppointmentDAO student_AppointmentDAO: student_AppointmentsDAO){
            Student_AppointmentDTO student_AppointmentDTO = student_AppointmentMapper.student_AppointmentDAOToStudent_AppointmentDTO(student_AppointmentDAO);
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
     */

    public Map<String,Object> saveStudent_Appointment(Student_AppointmentDTO student_AppointmentDTO){
        Map<String,Object> answer = new TreeMap<>();
        if(student_AppointmentDTO != null){
            StudentDAO studentDAO = iStudentRepository.findById(4).orElse(null);
            AppointmentDAO appointmentDAO = iAppointmentRepository.findById(student_AppointmentDTO.getAppointment_id()).orElse(null);

            Student_AppointmentDAO student_AppointmentDAO = student_AppointmentMapper.student_AppointmentDTOToStudent_AppointmentDAO(student_AppointmentDTO);
            student_AppointmentDAO.setStudent(studentDAO);
            student_AppointmentDAO.setAppointment(appointmentDAO);

            System.out.println("Este es studentAppointmentDAO");
            System.out.println(student_AppointmentDAO);

            iStudent_AppointmentRepository.save(student_AppointmentDAO);
            answer.put("message", "Student_Appointment saved successfully");
        }else{
            answer.put("error", "Not successful");
        }
        return answer;
    }

}