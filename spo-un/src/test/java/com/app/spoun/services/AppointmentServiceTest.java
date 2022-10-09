package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.domain.Appointment;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.repository.*;
import com.app.spoun.repository.IAppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private IAppointmentRepository iAppointmentRepository;

    @Mock
    private ITentativeScheduleRepository iTentativeScheduleRepository;

    @Mock
    private IScheduleRepository iScheduleRepository;

    @Mock
    private IStudentRepository iStudentRepository;

    @Mock
    private IRoomRepository iRoomRepository;

    @Mock
    private Patient patient;

    @Mock
    private TentativeSchedule tentativeSchedule;

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private Building building;

    @Mock
    private Professor professor;

    @Mock
    private AppointmentDTO appointmentDTO;

    @Mock
    private List<String> students;

    private FullAppointmentDTO fullAppointmentDTO;

    private Room room;

    private Role role;

    private Student student;

    private Appointment appointment;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);

        ArrayList<Student> studentsAL = new ArrayList<Student>();
        studentsAL.add(student);

        ArrayList<TentativeSchedule> tentativeSchedules = new ArrayList<TentativeSchedule>();
        tentativeSchedules.add(tentativeSchedule);

        fullAppointmentDTO = new FullAppointmentDTO();
        fullAppointmentDTO.setAppointment(appointmentDTO);

        role = new Role();
        role.setName("Student");

        room = new Room();
        room.setName("203");
        room.setBuilding(building);
        room.setAppointments(appointments);

        student = new Student();
        student.setAppointments(appointments);
        student.setName("Juanito");
        student.setUsername("usr21");
        student.setRole(role);
        student.setPassword("f456");
        student.setDocument_type("C.C.");
        student.setDocument_number("602939781");
        student.setProfessor(professor);

        appointment = new Appointment();
        appointment.setStart_time("2022-11-14 08:00:00");
        appointment.setEnd_time("2022-11-14 08:30:00");
        appointment.setRoom(room);
        appointment.setProfessor(professor);
        appointment.setStudents(studentsAL);
        appointment.setPatient(patient);
        appointment.setState("available");
        appointment.setTentativeSchedules(tentativeSchedules);

    }

    @Test
    void isAvailableSchedule(){
    }

    @Test
    void confirmAppointmentById(){
    }

    @Test
    void getAllAppointment(){
        Page<Appointment> appointments = new PageImpl<>(Arrays.asList(appointment));
        Mockito.when(iAppointmentRepository.findAll(any(Pageable.class))).thenReturn(appointments);
        assertNotNull(appointmentService.getAllAppointment(0, 10));
    }

    @Test
    void findAppointmentById() throws ParseException {
        Mockito.when(iAppointmentRepository.findById(any(Integer.class))).thenReturn(Optional.of(appointment));
        assertNotNull(appointmentService.findAppointmentById(1));
    }

    @Test
    void saveAppointment(){
        Mockito.when(iAppointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        assertNotNull(appointmentService.saveAppointment(fullAppointmentDTO));
    }

    @Test
    void editAppointment(){ assertNotNull(appointmentService.editAppointment(fullAppointmentDTO)); }

    @Test
    void deleteAppointment(){ assertNotNull(appointmentService.deleteAppointment(1)); }
}
