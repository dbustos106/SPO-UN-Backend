package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.AppointmentMapper;
import com.app.spoun.mappers.ProfessorMapper;
import com.app.spoun.mappers.StudentMapper;
import com.app.spoun.repository.*;

import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Transactional
@Service
@Slf4j
public class ProfessorService{

    private final IProfessorRepository iProfessorRepository;
    private final IStudentRepository iStudentRepository;
    private final IPatientRepository iPatientRepository;
    private final IAdminRepository iAdminRepository;
    private final IRoleRepository iRoleRepository;
    private final IAppointmentRepository iAppointmentRepository;
    private final EmailValidatorService emailValidatorService;
    private final EmailSenderService emailSenderService;
    private final ProfessorMapper professorMapper;
    private final StudentMapper studentMapper;
    private final AppointmentMapper appointmentMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ProfessorService(IProfessorRepository iProfessorRepository,
                            IStudentRepository iStudentRepository,
                            IPatientRepository iPatientRepository,
                            IAdminRepository iAdminRepository,
                            IRoleRepository iRoleRepository,
                            IAppointmentRepository iAppointmentRepository,
                            EmailValidatorService emailValidatorService,
                            EmailSenderService emailSenderService,
                            ProfessorMapper professorMapper,
                            StudentMapper studentMapper,
                            AppointmentMapper appointmentMapper,
                            PasswordEncoder passwordEncoder){
        this.iProfessorRepository = iProfessorRepository;
        this.iStudentRepository = iStudentRepository;
        this.iPatientRepository = iPatientRepository;
        this.iAdminRepository = iAdminRepository;
        this.iRoleRepository = iRoleRepository;
        this.iAppointmentRepository = iAppointmentRepository;
        this.emailValidatorService = emailValidatorService;
        this.emailSenderService = emailSenderService;
        this.professorMapper = professorMapper;
        this.studentMapper = studentMapper;
        this.appointmentMapper = appointmentMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public Map<String, Object> changePassword(String code, String password){
        Map<String, Object> answer = new TreeMap<>();

        Professor professor = iProfessorRepository.findByVerification_code(code).orElse(null);
        if(professor == null){
            throw new IllegalStateException("Invalid code");
        }else{
            professor.setVerification_code(null);
            professor.setPassword(password);
            iProfessorRepository.save(professor);
            answer.put("message", "Successful password change");
        }
        return answer;
    }

    public Map<String, Object> getProfessorScheduleByProfessorId(Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get appointments
        List<Appointment> appointments = iAppointmentRepository.getProfessorScheduleByProfessorId(id);

        // read schedules
        List<Map<String, Object>> listScheduleDTOS = new ArrayList<>();
        for(Appointment appointment : appointments){
            Map<String,Object> schedule = new TreeMap<>();
            schedule.put("start_time", appointment.getStart_time());
            schedule.put("end_time", appointment.getEnd_time());
            schedule.put("appointment_id", appointment.getId());
            listScheduleDTOS.add(schedule);
        }

        // return schedules
        answer.put("message", listScheduleDTOS);

        return answer;
    }

    public Map<String, Object> getAppointmentsByProfessorId(Integer idPage, Integer size, Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get page of appointments
        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findByProfessorId(id, page);

        // map all appointments
        List<AppointmentDTO> listAppointmentDTOS = new ArrayList<>();
        for(Appointment appointment : appointments){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            listAppointmentDTOS.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentDTOS = new PageImpl<>(listAppointmentDTOS);

        // return page of appointments
        answer.put("message", appointmentDTOS);

        return answer;
    }


    public Map<String, Object> getAllProfessor(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of professors
        Pageable page = PageRequest.of(idPage, size);
        Page<Professor> professors = iProfessorRepository.findAll(page);

        // map all professors
        List<ProfessorDTO> listProfessorDTOS = new ArrayList<>();
        for(Professor professor : professors){
            ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
            listProfessorDTOS.add(professorDTO);
        }
        Page<ProfessorDTO> professorDTOS = new PageImpl<>(listProfessorDTOS);

        // return page of professors
        answer.put("message", professorDTOS);

        return answer;
    }

    public Map<String, Object> findProfessorById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Professor professor = iProfessorRepository.findById(id).orElse(null);
        if(professor != null){
            ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
            answer.put("message", professorDTO);
        }else{
            throw new NotFoundException("Professor not found");
        }
        return answer;
    }

    public Map<String, Object> getStudentsByProfessorId(Integer idPage, Integer size, Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get page of students
        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findByProfessor_id(id, page);

        // map all students
        List<StudentDTO> listStudentDTOS = new ArrayList<>();
        for(Student student : students){
            StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
            listStudentDTOS.add(studentDTO);
        }
        Page<StudentDTO> studentDTOS = new PageImpl<>(listStudentDTOS);

        // return page of students
        answer.put("message", studentDTOS);

        return answer;
    }

    public Map<String, Object> saveProfessor(ProfessorDTO professorDTO) throws UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        if(professorDTO == null){
            throw new IllegalStateException("Request data missing");
        }else if(!emailValidatorService.test(professorDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }else if(iStudentRepository.existsByEmail(professorDTO.getEmail()) ||
                    iPatientRepository.existsByEmail(professorDTO.getEmail()) ||
                    iAdminRepository.existsByEmail(professorDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }else{
            // get role
            Role role = iRoleRepository.findByName("Professor").orElse(null);

            // map professor
            Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
            professor.setStudents(new ArrayList<>());
            professor.setAppointments(new ArrayList<>());
            professor.setRole(role);

            // encrypt password
            professor.setPassword(passwordEncoder.encode(professor.getPassword()));

            // create verification code and disable account
            String randomCode = RandomString.make(64);
            professor.setVerification_code(randomCode);
            professor.setEnabled(false);

            // save professor
            iProfessorRepository.save(professor);
            answer.put("message", "Professor saved successfully");

            String content = "Querid@ [[name]],<br>"
                    + "Por favor haga click en el siguiente link para verificar su cuenta:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Gracias,<br>"
                    + "Spo-un.";
            String subject = "Verifique su registro";
            String verifyURL = "http://localhost:8080/verifyAccount/professor/" + professor.getVerification_code();
            //String verifyURL = "http://spoun.app.s3-website-us-east-1.amazonaws.com/verifyAccount/professor/" + professor.getVerification_code();
            content = content.replace("[[name]]", professor.getName());
            content = content.replace("[[URL]]", verifyURL);
            emailSenderService.send(professor.getEmail(), subject, content);
        }
        return answer;
    }

    public Map<String, Object> verifyProfessor(String code){
        Map<String, Object> answer = new TreeMap<>();

        Professor professor = iProfessorRepository.findByVerification_code(code).orElse(null);
        if(professor == null){
            throw new IllegalStateException("Invalid code");
        }else{
            professor.setVerification_code(null);
            professor.setEnabled(true);
            iProfessorRepository.save(professor);
            answer.put("message", "Successful verification");
        }
        return answer;
    }

    public Map<String, Object> editProfessor(ProfessorDTO professorDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(professorDTO == null){
            throw new IllegalStateException("Request data missing");
        }else if(!emailValidatorService.test(professorDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }else if(iStudentRepository.existsByEmail(professorDTO.getEmail()) ||
                iPatientRepository.existsByEmail(professorDTO.getEmail()) ||
                iAdminRepository.existsByEmail(professorDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }else{
            Professor professor = iProfessorRepository.findById(professorDTO.getId()).orElse(null);
            if(professor == null){
                throw new NotFoundException("Professor not found");
            }else{
                // update professor
                professor.setName(professorDTO.getName());
                professor.setLast_name(professorDTO.getLast_name());
                professor.setDocument_type(professorDTO.getDocument_type());
                professor.setDocument_number(professorDTO.getDocument_number());

                iProfessorRepository.save(professor);
                answer.put("message", "Professor updated successfully");
            }
        }
        return answer;
    }

    public Map<String, Object> deleteProfessor(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(iProfessorRepository.existsById(id)){
            iProfessorRepository.deleteById(id);
            answer.put("message", "Professor deleted successfully");
        }else{
            throw new NotFoundException("Professor not found");
        }
        return answer;
    }

}

