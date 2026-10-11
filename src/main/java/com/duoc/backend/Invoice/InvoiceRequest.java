package com.duoc.backend.Invoice;

import com.duoc.backend.care.Care;
import com.duoc.backend.medication.Medication;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Datos de entrada para emitir una factura (evita exponer la entidad JPA, CWE-915).
 * Los cuidados y medicamentos se reciben solo por su id; el costo total lo calcula el servicio.
 */
public record InvoiceRequest(
        @NotBlank String patientName,
        @NotNull LocalDate date,
        LocalTime time,
        List<@Valid ItemRef> cares,
        List<@Valid ItemRef> medications) {

    /** Referencia a un cuidado o medicamento existente. */
    public record ItemRef(@NotNull Long id) {
    }

    public Invoice toEntity() {
        List<Care> careList = new ArrayList<>();
        if (cares != null) {
            for (ItemRef ref : cares) {
                Care care = new Care();
                care.setId(ref.id());
                careList.add(care);
            }
        }
        List<Medication> medicationList = new ArrayList<>();
        if (medications != null) {
            for (ItemRef ref : medications) {
                Medication medication = new Medication();
                medication.setId(ref.id());
                medicationList.add(medication);
            }
        }
        Invoice invoice = new Invoice(null, patientName, date, careList, medicationList);
        invoice.setTime(time);
        return invoice;
    }
}
