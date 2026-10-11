package com.duoc.backend.patient;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PositiveOrZero;

/** Datos de entrada para registrar un paciente (evita exponer la entidad JPA, CWE-915). */
public record PatientRequest(
        @NotBlank @Size(max = 255) String name,
        @NotBlank @Size(max = 255) String species,
        @Size(max = 255) String breed,
        @PositiveOrZero int age,
        @NotBlank @Size(max = 255) String owner) {

    public Patient toEntity() {
        Patient patient = new Patient();
        patient.setName(name);
        patient.setSpecies(species);
        patient.setBreed(breed);
        patient.setAge(age);
        patient.setOwner(owner);
        return patient;
    }
}
