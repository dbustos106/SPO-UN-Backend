package com.app.spoun.services;

import com.app.spoun.domain.Admin;
import com.app.spoun.domain.Role;
import com.app.spoun.dto.AdminDTO;
import com.app.spoun.mappers.AdminMapper;
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
public class AdminService{

    private final IAdminRepository iAdminRepository;
    private final IProfessorRepository iProfessorRepository;
    private final IStudentRepository iStudentRepository;
    private final IPatientRepository iPatientRepository;
    private final IRoleRepository iRoleRepository;
    private final EmailValidatorService emailValidatorService;
    private final EmailSenderService emailSenderService;
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(IAdminRepository iAdminRepository,
                        IProfessorRepository iProfessorRepository,
                        IStudentRepository iStudentRepository,
                        IPatientRepository iPatientRepository,
                        IRoleRepository iRoleRepository,
                        EmailValidatorService emailValidatorService,
                        EmailSenderService emailSenderService,
                        AdminMapper adminMapper,
                        PasswordEncoder passwordEncoder){
        this.iAdminRepository = iAdminRepository;
        this.iProfessorRepository = iProfessorRepository;
        this.iStudentRepository = iStudentRepository;
        this.iPatientRepository = iPatientRepository;
        this.iRoleRepository = iRoleRepository;
        this.emailValidatorService = emailValidatorService;
        this.emailSenderService = emailSenderService;
        this.adminMapper = adminMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public Map<String, Object> changePassword(String code, String password){
        Map<String, Object> answer = new TreeMap<>();

        Admin admin = iAdminRepository.findByVerification_code(code).orElse(null);
        if(admin == null){
            throw new IllegalStateException("Invalid code");
        }
        admin.setVerification_code(null);
        admin.setPassword(passwordEncoder.encode(password));
        iAdminRepository.save(admin);
        answer.put("message", "Successful password change");

        return answer;
    }

    public Map<String, Object> getAllAdmin(Integer idPage, Integer size){
        Map<String, Object> answer = new TreeMap<>();

        // get page of admins
        Pageable page = PageRequest.of(idPage, size);
        Page<Admin> admins = iAdminRepository.findAll(page);

        // map all admins
        List<AdminDTO> listAdminDTOS = new ArrayList<>();
        for(Admin admin : admins){
            AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
            listAdminDTOS.add(adminDTO);
        }
        Page<AdminDTO> adminDTOS = new PageImpl<>(listAdminDTOS);

        // return page of admins
        answer.put("message", adminDTOS);

        return answer;
    }

    public Map<String, Object> findAdminById(Long id){
        Map<String, Object> answer = new TreeMap<>();

        Admin admin = iAdminRepository.findById(id).orElse(null);
        if(admin == null){
            throw new NotFoundException("Admin not found");
        }
        AdminDTO adminDTO = adminMapper.adminToAdminDTO(admin);
        answer.put("message", adminDTO);

        return answer;
    }

    public Map<String, Object> saveAdmin(AdminDTO adminDTO) throws UnsupportedEncodingException {
        Map<String, Object> answer = new TreeMap<>();

        if(adminDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        if(!emailValidatorService.test(adminDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }
        if(iProfessorRepository.existsByEmail(adminDTO.getEmail()) ||
                iPatientRepository.existsByEmail(adminDTO.getEmail()) ||
                iStudentRepository.existsByEmail(adminDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }

        // get role
        Role role = iRoleRepository.findByName("Admin").orElse(null);

        // map admin
        Admin admin = adminMapper.adminDTOToAdmin(adminDTO);
        admin.setRole(role);

        // encrypt password
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));

        // create verification code and disable account
        String randomCode = RandomString.make(64);
        admin.setVerification_code(randomCode);
        admin.setEnabled(false);

        // save admin
        iAdminRepository.save(admin);
        answer.put("message", "Admin saved successfully");

        String content = "Querid@ [[name]],<br>"
                    + "Por favor haga click en el siguiente link para verificar su cuenta:<br>"
                    + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                    + "Gracias,<br>"
                    + "Spo-un.";
        String subject = "Verifique su registro";
        String verifyURL = "http://localhost:8080/verifyAccount/admin/" + admin.getVerification_code();
        //String verifyURL = "http://spoun.app.s3-website-us-east-1.amazonaws.com/verifyAccount/admin/" + admin.getVerification_code();
        content = content.replace("[[name]]", admin.getEmail());
        content = content.replace("[[URL]]", verifyURL);
        emailSenderService.send(admin.getEmail(), subject, content);

        return answer;
    }

    public Map<String, Object> verifyAdmin(String code){
        Map<String, Object> answer = new TreeMap<>();

        Admin admin = iAdminRepository.findByVerification_code(code).orElse(null);
        if(admin == null){
            throw new IllegalStateException("Invalid code");
        }
        admin.setEnabled(true);
        admin.setVerification_code(null);
        iAdminRepository.save(admin);
        answer.put("message", "Successful verification");

        return answer;
    }

    public Map<String, Object> editAdmin(AdminDTO adminDTO){
        Map<String, Object> answer = new TreeMap<>();

        if(adminDTO == null){
            throw new IllegalStateException("Request data missing");
        }
        if(!emailValidatorService.test(adminDTO.getEmail())){
            throw new IllegalStateException("Email not valid");
        }
        if(iProfessorRepository.existsByEmail(adminDTO.getEmail()) ||
                iPatientRepository.existsByEmail(adminDTO.getEmail()) ||
                iStudentRepository.existsByEmail(adminDTO.getEmail())){
            throw new IllegalStateException("Repeated email");
        }

        Admin admin = iAdminRepository.findById(adminDTO.getId()).orElse(null);
        if(admin == null){
            throw new NotFoundException("Admin not found");
        }
        // update admin
        admin.setEmail(adminDTO.getEmail());

        iAdminRepository.save(admin);
        answer.put("message", "Admin updated successfully");

        return answer;
    }

    public Map<String, Object> deleteAdmin(Long id){
        Map<String, Object> answer = new TreeMap<>();

        if(!iAdminRepository.existsById(id)){
            throw new NotFoundException("Admin not found");
        }
        iAdminRepository.deleteById(id);
        answer.put("message", "Admin deleted successfully");

        return answer;
    }

}