package com.duoc.backend;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Convierte errores esperables en respuestas controladas, en lugar de un 500:
 * - violación de integridad (por ejemplo, borrar un medicamento asociado a una factura) -> 409;
 * - datos de negocio inválidos (por ejemplo, una factura con cuidados inexistentes) -> 400.
 * El cuerpo es genérico y no expone detalles internos de la base de datos.
 */
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> handleDataIntegrity(DataIntegrityViolationException e) {
        return error(HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException e) {
        return error(HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<Map<String, Object>> error(HttpStatus status) {
        return ResponseEntity.status(status)
                .body(Map.of("status", status.value(), "error", status.getReasonPhrase()));
    }
}
