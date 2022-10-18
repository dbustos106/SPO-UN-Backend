package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.dto.StudentDTO;
import com.app.spoun.mappers.*;
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
public class StudentService {

    @Autowired
    private IStudentRepository iStudentRepository;
    @Autowired
    private IProfessorRepository iProfessorRepository;
    @Autowired
    private IPatientRepository iPatientRepository;
    @Autowired
    private IAdminRepository iAdminRepository;
    @Autowired
    private IRoleRepository iRoleRepository;
    @Autowired
    private ITentativeScheduleRepository iTentativeScheduleRepository;
    @Autowired
    private IAppointmentRepository iAppointmentRepository;
    @Autowired
    private EmailValidatorService emailValidatorService;
    @Autowired
    private EmailSenderService emailSenderService;

    private StudentMapper studentMapper = new StudentMapperImpl();
    private TentativeScheduleMapper tentativeScheduleMapper = new TentativeScheduleMapperImpl();
    private AppointmentMapper appointmentMapper = new AppointmentMapperImpl();

    private final PasswordEncoder passwordEncoder;


    public Map<String, Object> cancelAppointmentById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);

        if(appointment != null){
            appointment.setState("Canceled");
            iAppointmentRepository.save(appointment);
            answer.put("message", "Appointment canceled successfully");
        }else{
            answer.put("error", "No appointment found");
        }
        return answer;
    }

    public Map<String, Object> getStudentScheduleByStudentId(Long id){
        Map<String,Object> answer = new TreeMap<>();

        // get appointments
        List<Appointment> appointments = iAppointmentRepository.getStudentScheduleByStudentId(id);

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

    public Map<String, Object> getStudentUnconfirmedScheduleByStudentId(Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get schedules
        List<TentativeSchedule> tentativeSchedules = iTentativeScheduleRepository.getStudentUnconfirmedScheduleByStudentId(id);

        // map schedules
        List<TentativeScheduleDTO> listTentativeScheduleDTOS = new ArrayList<>();
        for(TentativeSchedule tentativeSchedule : tentativeSchedules){
            TentativeScheduleDTO tentativeScheduleDTO = tentativeScheduleMapper.tentativeScheduleToTentativeScheduleDTO(tentativeSchedule);
            listTentativeScheduleDTOS.add(tentativeScheduleDTO);
        }

        // return schedules
        if(listTentativeScheduleDTOS.size() != 0){
            answer.put("message", listTentativeScheduleDTOS);
        }else{
            answer.put("error", "No unconfirmed schedule found");
        }

        return answer;
    }

    public Map<String, Object> getAppointmentsByStudentId(Integer idPage, Integer size, Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get page of appointments
        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findByStudentId(id, page);

        // map all appointments
        List<AppointmentDTO> listAppointmentDTOS = new ArrayList<>();
        for(Appointment appointment : appointments){
            AppointmentDTO appointmentDTO = appointmentMapper.appointmentToAppointmentDTO(appointment);
            listAppointmentDTOS.add(appointmentDTO);
        }
        Page<AppointmentDTO> appointmentDTOS = new PageImpl<>(listAppointmentDTOS);

        // return page of appointments
        if(appointmentDTOS.getSize() != 0){
            answer.put("message", appointmentDTOS);
        }else {
            answer.put("error", "No appointment found");
        }
        return answer;
    }

    public Map<String, Object> getAllStudent(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of students
        Pageable page = PageRequest.of(idPage, size);
        Page<Student> students = iStudentRepository.findAll(page);

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
            answer.put("error", "No student found");
        }
        return answer;
    }

    public Map<String,Object> findStudentById(Long id){
        Map<String,Object> answer = new TreeMap<>();
        Student student = iStudentRepository.findById(id).orElse(null);
        StudentDTO studentDTO = studentMapper.studentToStudentDTO(student);
        if(studentDTO != null){
            answer.put("message", studentDTO);
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

    public Map<String,Object> saveStudent(StudentDTO studentDTO, String siteUrl) throws UnsupportedEncodingException, MessagingException {
        Map<String,Object> answer = new TreeMap<>();

        if(studentDTO == null){
            answer.put("error", "Student not saved");
        }else if(iProfessorRepository.existsByUsername(studentDTO.getUsername()) ||
                    iPatientRepository.existsByUsername(studentDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(studentDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(studentDTO.getEmail())){
            answer.put("error", "Email not valid");
        }else{
            // get role and professor
            Role role = iRoleRepository.findByName("Student").orElse(null);
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);

            // map student
            Student student = studentMapper.studentDTOToStudent(studentDTO);
            student.setRole(role);
            student.setProfessor(professor);
            student.setAppointments(new ArrayList<>());

            // encrypt password
            student.setPassword(passwordEncoder.encode(student.getPassword()));

            // create verification code and disable account
            String randomCode = RandomString.make(64);
            student.setVerification_code(randomCode);
            student.setEnabled(false);

            // save student
            iStudentRepository.save(student);
            answer.put("message", "Student saved successfully");

            String content = "Querido [[name]],<br>"
                    + "Por favor haga click en el siguiente link para verificar su cuenta:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Gracias,<br>"
                    + "Spo-un.";
            String subject = "Verifique su registro";
            String verifyURL = siteUrl + "/student/verify?code=" + student.getVerification_code();
            content = content.replace("[[name]]", student.getName());
            content = content.replace("[[URL]]", verifyURL);
            emailSenderService.send(student.getEmail(), subject, content);
        }
        return answer;
    }

    public Map<String, Object> verifyStudent(String code){
        Map<String, Object> answer = new TreeMap<>();
        Student student = iStudentRepository.findByVerification_code(code).orElse(null);

        if(student == null || student.isEnabled()){
            answer.put("error", "verify fail");
        }else{
            student.setVerification_code(null);
            student.setEnabled(true);
            iStudentRepository.save(student);
            answer.put("message", "verify success");
        }
        return answer;
    }

    public Map<String, Object> editStudent(StudentDTO studentDTO){
        Map<String, Object> answer = new TreeMap<>();
        if(studentDTO == null){
            answer.put("error", "Student not found");
        }else if(iProfessorRepository.existsByUsername(studentDTO.getUsername()) ||
                iPatientRepository.existsByUsername(studentDTO.getUsername()) ||
                iAdminRepository.existsByUsername(studentDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(studentDTO.getEmail())){
            answer.put("error", "Email not valid");
        }else{
            // get role and professor
            Role role = iRoleRepository.findByName("Student").orElse(null);
            Professor professor = iProfessorRepository.findById(studentDTO.getProfessor_id()).orElse(null);

            // update student
            Student student = studentMapper.studentDTOToStudent(studentDTO);
            student.setRole(role);
            student.setProfessor(professor);
            student.setAppointments(new ArrayList<>());

            // encrypt password
            student.setPassword(passwordEncoder.encode(student.getPassword()));

            iStudentRepository.save(student);
            answer.put("message", "Student updated successfully");
        }
        return answer;
    }

    public Map<String,Object> deleteStudent(Long id){
        Map<String,Object> answer = new TreeMap<>();
        if(iStudentRepository.existsById(id)){
            iStudentRepository.deleteById(id);
            answer.put("message", "Student deleted successfully");
        }else{
            answer.put("error", "Student not found");
        }
        return answer;
    }

}