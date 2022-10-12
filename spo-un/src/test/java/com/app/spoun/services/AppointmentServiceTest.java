package com.app.spoun.services;

import com.app.spoun.domain.*;
import com.app.spoun.domain.Appointment;
import com.app.spoun.dto.AppointmentDTO;
import com.app.spoun.dto.FullAppointmentDTO;
import com.app.spoun.dto.ScheduleDTO;
import com.app.spoun.dto.TentativeScheduleDTO;
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
    private IPatientRepository iPatientRepository;

    @Mock
    private IRoomRepository iRoomRepository;

    @Mock
    private ScheduleService scheduleService;

    @Mock
    private TentativeScheduleService tentativeScheduleService;

    @Mock
    private Patient patient;

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private Building building;

    @Mock
    private Professor professor;

    @Mock
    private AppointmentDTO appointmentDTO;

    @Mock
    private TentativeScheduleDTO tentativeScheduleDTO;

    private List<TentativeScheduleDTO> tentativeSchedules;

    private FullAppointmentDTO fullAppointmentDTO;

    private Room room;

    private Role role;

    private Student student;

    private Schedule schedule;

    private TentativeSchedule tentativeSchedule;

    private Appointment appointment;

    private List<Schedule> schedules;

    private ScheduleDTO scheduleDTO;

    private List<String> students;

    @BeforeEach
    void setUp() {

        MockitoAnnotations.openMocks(this);

        schedule = new Schedule();
        schedule.setRoom(room);
        schedule.setStart_time("11-11-2022 09:00:00");
        schedule.setEnd_time("11-11-2022 09:30:00");

        tentativeSchedules = new ArrayList<>();
        tentativeSchedules.add(tentativeScheduleDTO);

        tentativeSchedule = new TentativeSchedule();
        tentativeSchedule.setAppointment(appointment);
        tentativeSchedule.setStart_time("11-11-2022 09:00:00");
        tentativeSchedule.setEnd_time("11-11-2022 09:30:00");

        ArrayList<Appointment> appointments = new ArrayList<Appointment>();
        appointments.add(appointment);

        ArrayList<Student> studentsAL = new ArrayList<Student>();
        studentsAL.add(student);

        ArrayList<TentativeSchedule> tentativeSchedulesAL = new ArrayList<TentativeSchedule>();
        tentativeSchedulesAL.add(tentativeSchedule);

        schedules = new ArrayList<Schedule>();
        schedules.add(schedule);

        room = new Room();
        room.setName("203");
        room.setBuilding(building);
        room.setAppointments(appointments);

        scheduleDTO = new ScheduleDTO();
        scheduleDTO.setRoom_id(room.getId());
        scheduleDTO.setStart_time("11-11-2022 09:00:00");
        scheduleDTO.setEnd_time("11-11-2022 09:30:00");

        students = new ArrayList<String>();
        students.add("usr21");

        fullAppointmentDTO = new FullAppointmentDTO();
        fullAppointmentDTO.setAppointment(appointmentDTO);
        fullAppointmentDTO.setStudents(students);
        fullAppointmentDTO.setTentativeSchedules(tentativeSchedules);

        students = new ArrayList<String>();
        students = fullAppointmentDTO.getStudents();

        role = new Role();
        role.setName("Student");

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
        appointment.setRoom(room);
        appointment.setProfessor(professor);
        appointment.setStudents(studentsAL);
        appointment.setPatient(patient);
        appointment.setState("available");
        appointment.setTentativeSchedules(tentativeSchedulesAL);

    }

    @Test
    void isAvailableSchedule() throws ParseException {
        assertNotNull(appointmentService.isAvailableSchedule(schedules, "11-11-2022 09:00:00", "11-11-2022 09:30:00"));
    }

    @Test
    void confirmAppointmentById() throws ParseException {
        Mockito.when(iAppointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointment));
        assertNotNull(appointmentService.confirmAppointmentById(room.getId(), patient.getId(), scheduleDTO));
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
        Mockito.when(iStudentRepository.findByUsername(students.get(0))).thenReturn(Optional.ofNullable(student));
        Mockito.when(iAppointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        assertNotNull(appointmentService.saveAppointment(fullAppointmentDTO));
    }

    @Test
    void editAppointment(){ assertNotNull(appointmentService.editAppointment(fullAppointmentDTO)); }

    @Test
    void deleteAppointment(){ assertNotNull(appointmentService.deleteAppointment(1)); }
}
