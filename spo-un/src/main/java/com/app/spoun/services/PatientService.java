package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.dto.PatientDTO;
import com.app.spoun.mappers.PatientMapper;
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
public class PatientService{

    private IPatientRepository iPatientRepository;
    private IStudentRepository iStudentRepository;
    private IProfessorRepository iProfessorRepository;
    private IAdminRepository iAdminRepository;
    private IRoleRepository iRoleRepository;
    private IAppointmentRepository iAppointmentRepository;
    private EmailValidatorService emailValidatorService;
    private EmailSenderService emailSenderService;
    private PatientMapper patientMapper;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public PatientService(IPatientRepository iPatientRepository,
                          IStudentRepository iStudentRepository,
                          IProfessorRepository iProfessorRepository,
                          IAdminRepository iAdminRepository,
                          IRoleRepository iRoleRepository,
                          IAppointmentRepository iAppointmentRepository,
                          EmailValidatorService emailValidatorService,
                          EmailSenderService emailSenderService,
                          PatientMapper patientMapper,
                          PasswordEncoder passwordEncoder){
        this.iPatientRepository = iPatientRepository;
        this.iStudentRepository = iStudentRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iAdminRepository = iAdminRepository;
        this.iRoleRepository = iRoleRepository;
        this.iAppointmentRepository = iAppointmentRepository;
        this.emailValidatorService = emailValidatorService;
        this.emailSenderService = emailSenderService;
        this.patientMapper = patientMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public Map<String, Object> cancelAppointmentByAppointmentId(Long id) throws MessagingException, UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        // get appointment
        Appointment appointment = iAppointmentRepository.findById(id).orElse(null);

        if(appointment != null){
            appointment.setState("Canceled");
            iAppointmentRepository.save(appointment);
            answer.put("message", "Appointment canceled successfully");

            // get professor
            Professor professor = appointment.getProfessor();
            String emailProfessor = "Querid@ [[name]],<br>"
                    + "Su cita número [[id]] ha sido cancelada por el paciente.<br>"
                    + "Si tiene algúna queja o comentario, comuníquese con los estudiantes a cargo:<br>";

            // get patient
            Patient patient = appointment.getPatient();
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

        }else{
            answer.put("error", "No appointment found");
        }
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
        if(patientDTOS.getSize() != 0){
            answer.put("message", patientDTOS);
        }else{
            answer.put("error", "No patient found");
        }
        return answer;
    }

    public Map<String, Object> findPatientById(Long id){
        Map<String, Object> answer = new TreeMap<>();
        Patient patient = iPatientRepository.findById(id).orElse(null);
        PatientDTO patientDTO = patientMapper.patientToPatientDTO(patient);
        if(patientDTO != null){
            answer.put("message", patientDTO);
        }else{
            answer.put("error", "Patient not found");
        }
        return answer;
    }

    public Map<String, Object> savePatient(PatientDTO patientDTO, String siteUrl)
            throws UnsupportedEncodingException, MessagingException {
        Map<String, Object> answer = new TreeMap<>();

        if(patientDTO == null){
            answer.put("error", "Patient not saved");
        }else if(iProfessorRepository.existsByUsername(patientDTO.getUsername()) ||
                    iStudentRepository.existsByUsername(patientDTO.getUsername()) ||
                    iAdminRepository.existsByUsername(patientDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(patientDTO.getEmail())){
            answer.put("error", "Email not valid");
        }else{
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
            String verifyURL = siteUrl + "/patient/verify?code=" + patient.getVerification_code();
            content = content.replace("[[name]]", patient.getName());
            content = content.replace("[[URL]]", verifyURL);
            emailSenderService.send(patient.getEmail(), subject, content);
        }
        return answer;
    }

    public Map<String, Object> verifyPatient(String code){
        Map<String, Object> answer = new TreeMap<>();
        Patient patient = iPatientRepository.findByVerification_code(code).orElse(null);

        if(patient == null || patient.isEnabled()){
            answer.put("error", "verify fail");
        }else{
            patient.setVerification_code(null);
            patient.setEnabled(true);
            iPatientRepository.save(patient);
            answer.put("message", "verify success");
        }
        return answer;
    }

    public Map<String, Object> editPatient(PatientDTO patientDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(patientDTO == null){
            answer.put("error", "Patient not found");
        }else if(iProfessorRepository.existsByUsername(patientDTO.getUsername()) ||
                iStudentRepository.existsByUsername(patientDTO.getUsername()) ||
                iAdminRepository.existsByUsername(patientDTO.getUsername())){
            answer.put("error", "Repeated username");
        }else if(!emailValidatorService.test(patientDTO.getEmail())){
            answer.put("error", "Email not valid");
        }else{
            // get role
            Role role = iRoleRepository.findByName("Patient").orElse(null);

            // update patient
            Patient patient = patientMapper.patientDTOToPatient(patientDTO);
            patient.setAntecedents(new ArrayList<>());
            patient.setAppointments(new ArrayList<>());
            patient.setRole(role);

            // encrypt password
            patient.setPassword(passwordEncoder.encode(patient.getPassword()));

            iPatientRepository.save(patient);
            answer.put("message", "Patient updated successfully");
        }
        return answer;
    }

    public Map<String, Object> deletePatient(Long id){
        Map<String, Object> answer = new TreeMap<>();
        if(iPatientRepository.existsById(id)){
            iPatientRepository.deleteById(id);
            answer.put("message", "Patient deleted successfully");
        }else{
            answer.put("error", "Patient not found");
        }
        return answer;
    }

}