package com.duoc.backend;

import com.duoc.backend.Invoice.Invoice;
import com.duoc.backend.Invoice.InvoiceRequest;
import com.duoc.backend.appointment.Appointment;
import com.duoc.backend.appointment.AppointmentRequest;
import com.duoc.backend.patient.Patient;
import com.duoc.backend.patient.PatientRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de los DTOs de entrada: copian solo los campos permitidos a la entidad,
 * de modo que el cliente no puede fijar el id ni el costo total (asignación masiva).
 */
class RequestDtoTest {

    @Test
    void testPatientRequestNoAsignaId() {
        Patient patient = new PatientRequest("Firulais", "Perro", "Quiltro", 4, "Juan").toEntity();

        assertNull(patient.getId());
        assertEquals("Firulais", patient.getName());
        assertEquals("Perro", patient.getSpecies());
        assertEquals("Quiltro", patient.getBreed());
        assertEquals(4, patient.getAge());
        assertEquals("Juan", patient.getOwner());
    }

    @Test
    void testAppointmentRequestNoAsignaId() {
        Appointment appointment = new AppointmentRequest(
                LocalDate.of(2026, 10, 12), LocalTime.of(9, 0), "Control", "Dr. Rojas").toEntity();

        assertNull(appointment.getId());
        assertEquals(LocalDate.of(2026, 10, 12), appointment.getDate());
        assertEquals("Control", appointment.getReason());
    }

    @Test
    void testInvoiceRequestSoloTomaIdsYNoElTotal() {
        InvoiceRequest request = new InvoiceRequest("Firulais", LocalDate.of(2026, 10, 12), LocalTime.of(11, 0),
                List.of(new InvoiceRequest.ItemRef(1L), new InvoiceRequest.ItemRef(2L)),
                List.of(new InvoiceRequest.ItemRef(3L)));

        Invoice invoice = request.toEntity();

        assertNull(invoice.getId());
        assertNull(invoice.getTotalCost(), "el total lo calcula el servicio, no el cliente");
        assertEquals(2, invoice.getCares().size());
        assertEquals(2L, invoice.getCares().get(1).getId());
        assertEquals(3L, invoice.getMedications().get(0).getId());
        assertEquals(LocalTime.of(11, 0), invoice.getTime());
    }

    @Test
    void testInvoiceRequestSinItemsGeneraListasVacias() {
        Invoice invoice = new InvoiceRequest("Firulais", LocalDate.of(2026, 10, 12), null, null, null).toEntity();

        assertTrue(invoice.getCares().isEmpty());
        assertTrue(invoice.getMedications().isEmpty());
    }
}
