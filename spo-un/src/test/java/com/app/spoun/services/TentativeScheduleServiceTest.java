package com.app.spoun.services;

import com.app.spoun.domain.Appointment;
import com.app.spoun.domain.TentativeSchedule;
import com.app.spoun.dto.TentativeScheduleDTO;
import com.app.spoun.repository.IAppointmentRepository;
import com.app.spoun.repository.ITentativeScheduleRepository;
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

import java.util.Arrays;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TentativeScheduleServiceTest {

    @Mock
    private ITentativeScheduleRepository iTentativeScheduleRepository;

    @Mock
    private Appointment appointment;

    @Mock
    private IAppointmentRepository iAppointmentRepository;

    @InjectMocks
    private TentativeScheduleService tentativeScheduleService;

    private TentativeSchedule tentativeSchedule;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        tentativeSchedule = new TentativeSchedule();
        tentativeSchedule.setAppointment(appointment);
        tentativeSchedule.setStart_time("2022-11-14 08:00:00");
        tentativeSchedule.setEnd_time("2022-11-14 08:30:00");
    }

    @Test
    void getAllTentativeSchedule(){
        Page<TentativeSchedule> tentativeSchedules = new PageImpl<>(Arrays.asList(tentativeSchedule));
        Mockito.when(iTentativeScheduleRepository.findAll(any(Pageable.class))).thenReturn(tentativeSchedules);
        assertNotNull(tentativeScheduleService.getAllTentativeSchedule(0, 10));
    }

    @Test
    void findTentativeScheduleById(){
        Mockito.when(iTentativeScheduleRepository.findById(any(Integer.class))).thenReturn(Optional.of(tentativeSchedule));
        assertNotNull(tentativeScheduleService.findTentativeScheduleById(1));
    }

    @Test
    void saveTentativeSchedule(){
        Mockito.when(iTentativeScheduleRepository.save(any(TentativeSchedule.class))).thenReturn(tentativeSchedule);
        assertNotNull(tentativeScheduleService.saveTentativeSchedule(new TentativeScheduleDTO()));
    }

    @Test
    void editTentativeSchedule(){ assertNotNull(tentativeScheduleService.editTentativeSchedule(new TentativeScheduleDTO())); }

    @Test
    void deleteTentativeSchedule(){ assertNotNull(tentativeScheduleService.deleteTentativeSchedule(1)); }
}
