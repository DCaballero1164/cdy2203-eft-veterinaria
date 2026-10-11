package com.duoc.backend;

import com.duoc.backend.medication.Medication;
import com.duoc.backend.medication.MedicationController;
import com.duoc.backend.medication.MedicationRepository;
import com.duoc.backend.medication.MedicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias propuestas para el package com.duoc.backend.medication
 * (tercero de menor cobertura según JaCoCo, 27 %).
 * Cubren los 4 métodos CRUD de MedicationService y los 4 de MedicationController;
 * probar solo el servicio dejaría el package en 57,5 %, bajo el 60 % exigido.
 */
class MedicationPackageTest {

    private MedicationRepository repository;
    private MedicationController controller;
    private Medication medication;

    /**
     * Arma la cadena repositorio simulado -> servicio real -> controlador real,
     * de modo que cada llamada al controlador ejecuta también el servicio.
     */
    @BeforeEach
    void setUp() {
        repository = mock(MedicationRepository.class);
        MedicationService service = new MedicationService();
        controller = new MedicationController();
        ReflectionTestUtils.setField(service, "medicationRepository", repository);
        ReflectionTestUtils.setField(controller, "medicationService", service);

        medication = new Medication("Antiparasitario", 12000.0);
        medication.setId(1L);
    }

    /** Verifica que la entidad conserve los valores asignados por sus setters. */
    @Test
    void testEntityGettersAndSetters() {
        medication.setName("Antibiotico");
        medication.setCost(9500.0);

        assertEquals(1L, medication.getId());
        assertEquals("Antibiotico", medication.getName());
        assertEquals(9500.0, medication.getCost());
    }

    /** Verifica que el listado de medicamentos llegue desde el repositorio. */
    @Test
    void testGetAllMedications() {
        when(repository.findAll()).thenReturn(List.of(medication));

        assertEquals(1, controller.getAllMedications().size());
    }

    /** Verifica que la búsqueda por id retorne el medicamento registrado. */
    @Test
    void testGetMedicationById() {
        when(repository.findById(1L)).thenReturn(Optional.of(medication));

        assertSame(medication, controller.getMedicationById(1L));
    }

    /** Verifica que el guardado retorne el medicamento persistido por el repositorio. */
    @Test
    void testSaveMedication() {
        when(repository.save(medication)).thenReturn(medication);

        assertSame(medication, controller.saveMedication(medication));
    }

    /** Verifica que la eliminación invoque deleteById con el id recibido. */
    @Test
    void testDeleteMedication() {
        controller.deleteMedication(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
