package com.duoc.backend.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

/** Datos de entrada para agendar una cita (evita exponer la entidad JPA, CWE-915). */
public record AppointmentRequest(
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotBlank @Size(max = 255) String reason,
        @NotBlank @Size(max = 255) String veterinarian) {

    public Appointment toEntity() {
        Appointment appointment = new Appointment();
        appointment.setDate(date);
        appointment.setTime(time);
        appointment.setReason(reason);
        appointment.setVeterinarian(veterinarian);
        return appointment;
    }
}
