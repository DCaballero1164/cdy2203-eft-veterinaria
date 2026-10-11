package com.duoc.backend.medication;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** Datos de entrada para registrar un medicamento (evita exponer la entidad JPA, CWE-915). */
public record MedicationRequest(@NotBlank String name, @NotNull @PositiveOrZero Double cost) {

    public Medication toEntity() {
        return new Medication(name, cost);
    }
}
