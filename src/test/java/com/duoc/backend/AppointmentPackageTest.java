package com.duoc.backend;

import com.duoc.backend.appointment.Appointment;
import com.duoc.backend.appointment.AppointmentController;
import com.duoc.backend.appointment.AppointmentRepository;
import com.duoc.backend.appointment.AppointmentRequest;
import com.duoc.backend.appointment.AppointmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias propuestas para el package com.duoc.backend.appointment,
 * el de menor cobertura según JaCoCo (10 %).
 * Cubren los métodos identificados en el informe: los 4 métodos CRUD de
 * AppointmentService, los 4 de AppointmentController y los getters/setters de
 * Appointment, con lo que el package supera el 60 % exigido.
 * No requieren base de datos: el repositorio se reemplaza por un mock de Mockito.
 */
class AppointmentPackageTest {

    private AppointmentRepository repository;
    private AppointmentController controller;
    private Appointment appointment;

    /**
     * Arma la cadena repositorio simulado -> servicio real -> controlador real.
     * Como las dependencias se inyectan con @Autowired sobre campos privados,
     * se asignan con ReflectionTestUtils sin levantar el contexto de Spring.
     */
    @BeforeEach
    void setUp() {
        repository = mock(AppointmentRepository.class);
        AppointmentService service = new AppointmentService();
        controller = new AppointmentController();
        ReflectionTestUtils.setField(service, "appointmentRepository", repository);
        ReflectionTestUtils.setField(controller, "appointmentService", service);

        appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDate(LocalDate.of(2025, 10, 5));
        appointment.setTime(LocalTime.of(10, 30));
        appointment.setReason("Vacunacion");
        appointment.setVeterinarian("Dra. Soto");
    }

    /** Verifica que la entidad conserve los valores asignados por sus setters. */
    @Test
    void testEntityGettersAndSetters() {
        assertEquals(1L, appointment.getId());
        assertEquals(LocalDate.of(2025, 10, 5), appointment.getDate());
        assertEquals(LocalTime.of(10, 30), appointment.getTime());
        assertEquals("Vacunacion", appointment.getReason());
        assertEquals("Dra. Soto", appointment.getVeterinarian());
    }

    /** Verifica que el listado de citas llegue desde el repositorio hasta el controlador. */
    @Test
    void testGetAllAppointments() {
        when(repository.findAll()).thenReturn(List.of(appointment));

        List<Appointment> result = controller.getAllAppointments();

        assertEquals(1, result.size());
        verify(repository, times(1)).findAll();
    }

    /** Verifica que la búsqueda por id retorne la cita registrada. */
    @Test
    void testGetAppointmentById() {
        when(repository.findById(1L)).thenReturn(Optional.of(appointment));

        assertSame(appointment, controller.getAppointmentById(1L));
    }

    /** Verifica que el guardado delegue en el repositorio y retorne la cita guardada. */
    @Test
    void testSaveAppointment() {
        when(repository.save(any(Appointment.class))).thenReturn(appointment);
        AppointmentRequest request = new AppointmentRequest(
                LocalDate.of(2025, 10, 5), LocalTime.of(10, 30), "Vacunacion", "Dra. Soto");

        assertSame(appointment, controller.saveAppointment(request));
        verify(repository, times(1)).save(any(Appointment.class));
    }

    /** Verifica que la eliminación invoque deleteById con el id recibido. */
    @Test
    void testDeleteAppointment() {
        controller.deleteAppointment(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
