package com.duoc.backend;

import com.duoc.backend.care.Care;
import com.duoc.backend.care.CareController;
import com.duoc.backend.care.CareRepository;
import com.duoc.backend.care.CareRequest;
import com.duoc.backend.care.CareService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias propuestas para el package com.duoc.backend.care
 * (segundo de menor cobertura según JaCoCo, 25 %).
 * Cubren los 4 métodos CRUD de CareService y los 4 de CareController.
 * A diferencia de los otros módulos, CareController no usa el servicio sino el
 * repositorio directamente, por lo que ambas clases se prueban por separado.
 */
class CarePackageTest {

    private CareRepository repository;
    private CareService service;
    private CareController controller;
    private Care care;

    /**
     * Inyecta el mismo repositorio simulado en el servicio y en el controlador,
     * y define las respuestas que el mock entrega en todas las pruebas.
     */
    @BeforeEach
    void setUp() {
        repository = mock(CareRepository.class);
        service = new CareService();
        controller = new CareController();
        ReflectionTestUtils.setField(service, "careRepository", repository);
        ReflectionTestUtils.setField(controller, "careRepository", repository);

        care = new Care("Consulta general", 15000.0);
        care.setId(1L);
        when(repository.findAll()).thenReturn(List.of(care));
        when(repository.findById(1L)).thenReturn(Optional.of(care));
        when(repository.save(any(Care.class))).thenReturn(care);
    }

    /** Verifica que la entidad conserve los valores asignados por sus setters. */
    @Test
    void testEntityGettersAndSetters() {
        care.setName("Bano");
        care.setCost(8000.0);

        assertEquals(1L, care.getId());
        assertEquals("Bano", care.getName());
        assertEquals(8000.0, care.getCost());
    }

    /** Recorre las operaciones CRUD del servicio: listar, buscar, guardar y eliminar. */
    @Test
    void testServiceCrud() {
        assertEquals(1, service.getAllCares().size());
        assertSame(care, service.getCareById(1L));
        assertSame(care, service.saveCare(care));
        service.deleteCare(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    /** Recorre las operaciones CRUD expuestas por el controlador REST /care. */
    @Test
    void testControllerCrud() {
        assertEquals(1, controller.getAllCares().size());
        assertSame(care, controller.getCareById(1L));
        assertSame(care, controller.saveCare(new CareRequest("Consulta general", 15000.0)));
        controller.deleteCare(1L);

        verify(repository, times(1)).deleteById(1L);
    }
}
