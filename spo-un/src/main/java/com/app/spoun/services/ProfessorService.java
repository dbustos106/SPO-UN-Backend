package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.Professor;
import com.app.spoun.domain.Role;
import com.app.spoun.domain.Student;
import com.app.spoun.dto.ProfessorDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.ProfessorMapper;
import com.app.spoun.mappers.StudentMapper;
import com.app.spoun.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.utility.RandomString;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class ProfessorService{

    private IProfessorRepository iProfessorRepository;
    private IStudentRepository iStudentRepository;
    private IPatientRepository iPatientRepository;
    private IAdminRepository iAdminRepository;
    private IRoleRepository iRoleRepository;
    private IAppointmentRepository iAppointmentRepository;
    private EmailValidatorService emailValidatorService;
    private EmailSenderService emailSenderService;
    private ProfessorMapper professorMapper;
    private StudentMapper studentMapper;
    private PasswordEncoder passwordEncoder;

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
        this.passwordEncoder = passwordEncoder;
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
            listScheduleDTOS.add(schedule);
        }

        // return schedules
        if(listScheduleDTOS.size() != 0){
            answer.put("message", listScheduleDTOS);
        }else{
            answer.put("error", "No schedule found");
        }
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
        if(professorDTOS.getSize() != 0){
            answer.put("message", professorDTOS);
        }else {
            answer.put("error", "No professor found");
        }
        return answer;
    }

    public Map<String, Object> findProfessorById(Long id){
        Map<String, Object> answer = new TreeMap<>();
        Professor professor = iProfessorRepository.findById(id).orElse(null);
        ProfessorDTO professorDTO = professorMapper.professorToProfessorDTO(professor);
        if(professorDTO != null){
            answer.put("message", professorDTO);
        }else{
            answer.put("error", "Professor not found");
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
        if(studentDTOS.getSize() != 0){
            answer.put("message", studentDTOS);
        }else {
            answer.put("error", "No students found under this professor");
        }
        return answer;
    }

    public Map<String, Object> saveProfessor(ProfessorDTO professorDTO, String siteUrl) throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();

        if(professorDTO == null){
            answer.put("error", "Professor not saved");
        }else if(iStudentRepository.existsByUsername(professorDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(professorDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(professorDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(professorDTO.getEmail())){
            answer.put("error", "Email not valid");
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
            String verifyURL = siteUrl + "/professor/verify?code=" + professor.getVerification_code();
            content = content.replace("[[name]]", professor.getName());
            content = content.replace("[[URL]]", verifyURL);
            emailSenderService.send(professor.getEmail(), subject, content);
        }
        return answer;
    }

    public Map<String, Object> verifyProfessor(String code){
        Map<String, Object> answer = new TreeMap<>();
        Professor professor = iProfessorRepository.findByVerification_code(code).orElse(null);

        if(professor == null || professor.isEnabled()){
            answer.put("error", "verify fail");
        }else{
            professor.setVerification_code(null);
            professor.setEnabled(true);
            iProfessorRepository.save(professor);
            answer.put("message", "verify success");
        }
        return answer;
    }

    public Map<String, Object> editProfessor(ProfessorDTO professorDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(professorDTO == null){
            answer.put("error", "Professor not found");
        }else if(iStudentRepository.existsByUsername(professorDTO.getUsername()) ||
                iPatientRepository.existsByUsername(professorDTO.getUsername()) ||
                iAdminRepository.existsByUsername(professorDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(professorDTO.getEmail())){
            answer.put("error", "Email not valid");
        }else{
            // get role
            Role role = iRoleRepository.findByName("Professor").orElse(null);

            // update professor
            Professor professor = professorMapper.professorDTOToProfessor(professorDTO);
            professor.setStudents(new ArrayList<>());
            professor.setAppointments(new ArrayList<>());
            professor.setRole(role);

            // encrypt password
            professor.setPassword(passwordEncoder.encode(professor.getPassword()));

            iProfessorRepository.save(professor);
            answer.put("message", "Professor updated successfully");
        }
        return answer;
    }

    public Map<String, Object> deleteProfessor(Long id){
        Map<String, Object> answer = new TreeMap<>();
        if(iProfessorRepository.existsById(id)){
            iProfessorRepository.deleteById(id);
            answer.put("message", "Professor deleted successfully");
        }else{
            answer.put("error", "Professor not found");
        }
        return answer;
    }

}

