package com.duoc.backend;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Datos de entrada del login. Se usa un DTO en lugar de la entidad User para que el cliente
 * no pueda asignar campos internos de la entidad (asignación masiva, CWE-915).
 */
public record LoginRequest(@NotBlank @Size(max = 255) String username, @NotBlank @Size(max = 255) String password) {
}
