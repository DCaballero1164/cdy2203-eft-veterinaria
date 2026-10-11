package com.duoc.backend.care;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

/** Datos de entrada para registrar un cuidado (evita exponer la entidad JPA, CWE-915). */
public record CareRequest(@NotBlank String name, @NotNull @PositiveOrZero Double cost) {

    public Care toEntity() {
        return new Care(name, cost);
    }
}
