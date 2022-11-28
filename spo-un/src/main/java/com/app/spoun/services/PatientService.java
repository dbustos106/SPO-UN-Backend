package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.mappers.AppointmentMapper;
import com.app.spoun.mappers.PatientMapper;
import com.app.spoun.repository.*;

import lombok.extern.slf4j.Slf4j;

import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Transactional
@Service
@Slf4j
public class PatientService{

    private final IPatientRepository iPatientRepository;
    private final IStudentRepository iStudentRepository;
    private final IProfessorRepository iProfessorRepository;
    private final IAdminRepository iAdminRepository;
    private final IRoleRepository iRoleRepository;
    private final IScheduleRepository iScheduleRepository;
    private final IAppointmentRepository iAppointmentRepository;
    private final EmailValidatorService emailValidatorService;
    private final EmailSenderService emailSenderService;
    private final PatientMapper patientMapper;
    private final AppointmentMapper appointmentMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PatientService(IPatientRepository iPatientRepository,
                          IStudentRepository iStudentRepository,
                          IProfessorRepository iProfessorRepository,
                          IAdminRepository iAdminRepository,
                          IRoleRepository iRoleRepository,
                          IScheduleRepository iScheduleRepository,
                          IAppointmentRepository iAppointmentRepository,
                          EmailValidatorService emailValidatorService,
                          EmailSenderService emailSenderService,
                          PatientMapper patientMapper,
                          AppointmentMapper appointmentMapper,
                          PasswordEncoder passwordEncoder){
        this.iPatientRepository = iPatientRepository;
        this.iStudentRepository = iStudentRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iAdminRepository = iAdminRepository;
        this.iRoleRepository = iRoleRepository;
        this.iScheduleRepository = iScheduleRepository;
        this.iAppointmentRepository = iAppointmentRepository;
        this.emailValidatorService = emailValidatorService;
        this.emailSenderService = emailSenderService;
        this.patientMapper = patientMapper;
        this.appointmentMapper = appointmentMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public Map<String, Object> changePassword(String code, String password){
        Map<String, Object> answer = new TreeMap<>();

        Patient patient = iPatientRepository.findByVerification_code(code).orElse(null);
        if(patient == null){
            throw new IllegalStateException("Invalid code");
        }
        patient.setVerification_code(null);
        patient.setPassword(passwordEncoder.encode(password));
        iPatientRepository.save(patient);
        answer.put("message", "Successful password change");

        return answer;
    }

    public Map<String, Object> cancelAppointmentByAppointmentId(Long id) throws UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);
        if(appointment == null) {
            throw new NotFoundException("Appointment not found");
        }

        // get professor and patient
        Professor professor = appointment.getProfessor();
        Patient patient = appointment.getPatient();

        // delete schedule
        if(appointment.getStart_time() != null) {
            iScheduleRepository.deleteByStart_timeAndRoom_id(appointment.getStart_time(), appointment.getRoom().getId());
        }

        // cancel appointment
        appointment.setState("Canceled");
        appointment.setStart_time(null);
        appointment.setEnd_time(null);
        appointment.setPatient(null);
        iAppointmentRepository.save(appointment);
        answer.put("message", "Appointment canceled successfully");

        String emailProfessor = "Querid@ [[name]],<br>"
                + "Su cita número [[id]] ha sido cancelada por el paciente.<br>"
                + "Si tiene algúna queja o comentario, comuníquese con los estudiantes a cargo:<br>";

        String emailPatient = "Querid@ [[name]],<br>"
                + "Su cita número [[id]] ha sido cancelada con éxito.<br>"
                + "Si desea comunicarse con los estudiantes encargados de la cita, comuniquese con:<br>";

        // get students
        List<Student> students = iStudentRepository.findByAppointment_id(id);
        for(Student student : students){
            String emailStudent = "Querid@ [[name]],<br>"
                    + "Su cita número [[id]] ha sido cancelada por el paciente.<br>"
                    + "Gracias,<br>"
                    + "Spo-un.";
            String subjectStudent = "Su cita ha sido cancelada";
            emailStudent = emailStudent.replace("[[name]]", student.getName());
            emailStudent = emailStudent.replace("[[id]]", id.toString());
            emailSenderService.send(student.getEmail(), subjectStudent, emailStudent);

            emailProfessor += student.getName() + ":" + student.getEmail() + ".<br>";
            emailPatient += student.getName() + ":" + student.getEmail() + ".<br>";
        }

        // send email to professor
        emailProfessor += "Gracias,<br>" + "Spo-un.";
        String subjectProfessor = "Su cita ha sido cancelada";
        emailProfessor = emailProfessor.replace("[[name]]", professor.getName());
        emailProfessor = emailProfessor.replace("[[id]]", id.toString());
        emailSenderService.send(professor.getEmail(), subjectProfessor, emailProfessor);

        // send email to patient
        emailPatient += "Gracias,<br>" + "Spo-un.";
        String subjectPatient = "Su cita ha sido cancelada";
        emailPatient = emailPatient.replace("[[name]]", patient.getName());
        emailPatient = emailPatient.replace("[[id]]", id.toString());
        emailSenderService.send(patient.getEmail(), subjectPatient, emailPatient);

        return answer;
    }

    public Map<String, Object> getPatientScheduleByPatientId(Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get appointments
        List<Appointment> appointments = iAppointmentRepository.getPatientScheduleByPatientId(id);

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

    public Map<String, Object> getAppointmentsByPatientId(Integer idPage, Integer size, Long id){
        Map<String, Object> answer = new TreeMap<>();

        // get page of appointments
        Pageable page = PageRequest.of(idPage, size);
        Page<Appointment> appointments = iAppointmentRepository.findByPatientId(id, page);

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

    public Map<String, Object> getAllPatient(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of patients
        Pageable page = PageRequest.of(idPage, size);
        Page<Patient> patients = iPatientRepository.findAll(page);

        // map all patients
        List<PatientDTO> listPatientDTOS = new ArrayList<>();
        for(Patient patient : patients){
            PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
            listPatientDTOS.add(patientDTO);
        }
        Page<PatientDTO> patientDTOS = new PageImpl<>(listPatientDTOS);

        // return page of patients
        answer.put("message", patientDTOS);

        return answer;
    }

    public Map<String, Object> findPatientById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Patient patient = iPatientRepository.findById(id).orElse(null);
        if(patient == null){
            throw new NotFoundException("Patient not found");
        }
        PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
        answer.put("message", patientDTO);

        return answer;
    }

    public Map<String, Object> savePatient(PatientDTO patientDTO) throws UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        if(patientDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        if(!emailValidatorService.test(patientDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }
        if(iProfessorRepository.existsByEmail(patientDTO.getEmail()) ||
                iStudentRepository.existsByEmail(patientDTO.getEmail()) ||
                iAdminRepository.existsByEmail(patientDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }

        // get role
        Role role = iRoleRepository.findByName("Patient").orElse(null);

        // map patient
        Patient patient = patientMapper.patientDTOToPatient(patientDTO);
        patient.setAntecedents(new ArrayList<>());
        patient.setAppointments(new ArrayList<>());
        patient.setRole(role);

        // encrypt password
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        // create verification code and disable account
        String randomCode = RandomString.make(64);
        patient.setVerification_code(randomCode);
        patient.setEnabled(false);

        // save patient
        iPatientRepository.save(patient);
        answer.put("message", "Patient saved successfully");

        String content = "Querid@ [[name]],<br>"
                + "Por favor haga click en el siguiente link para verificar su cuenta:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Gracias,<br>"
                + "Spo-un.";
        String subject = "Verifique su registro";
        String verifyURL = "http://localhost:8080/verifyAccount/patient/" + patient.getVerification_code();
        //String verifyURL = "http://spoun.app.s3-website-us-east-1.amazonaws.com/verifyAccount/patient/" + patient.getVerification_code();
        content = content.replace("[[name]]", patient.getName());
        content = content.replace("[[URL]]", verifyURL);
        emailSenderService.send(patient.getEmail(), subject, content);

        return answer;
    }

    public Map<String, Object> verifyPatient(String code){
        Map<String, Object> answer = new TreeMap<>();

        Patient patient = iPatientRepository.findByVerification_code(code).orElse(null);
        if(patient == null){
            throw new IllegalStateException("Invalid code");
        }
        patient.setEnabled(true);
        patient.setVerification_code(null);
        iPatientRepository.save(patient);
        answer.put("message", "Successful verification");

        return answer;
    }

    public Map<String, Object> editPatient(PatientDTO patientDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(patientDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        if(!emailValidatorService.test(patientDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }
        if(iProfessorRepository.existsByEmail(patientDTO.getEmail()) ||
                iStudentRepository.existsByEmail(patientDTO.getEmail()) ||
                iAdminRepository.existsByEmail(patientDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }

        Patient patient = iPatientRepository.findById(patientDTO.getId()).orElse(null);
        if(patient == null) {
            throw new NotFoundException("Patient not found");
        }
        // update patient
        patient.setName(patientDTO.getName());
        patient.setLast_name(patientDTO.getLast_name());
        patient.setDocument_type(patientDTO.getDocument_type());
        patient.setDocument_number(patientDTO.getDocument_number());
        patient.setBlood_type(patientDTO.getBlood_type());
        patient.setGender(patientDTO.getGender());
        patient.setAge(patientDTO.getAge());

        iPatientRepository.save(patient);
        answer.put("message", "Patient updated successfully");

        return answer;
    }

    public Map<String, Object> deletePatient(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(!iPatientRepository.existsById(id)){
            throw new NotFoundException("Patient not found");
        }
        iPatientRepository.deleteById(id);
        answer.put("message", "Patient deleted successfully");

        return answer;
    }

}