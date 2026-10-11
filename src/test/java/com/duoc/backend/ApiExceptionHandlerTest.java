package com.duoc.backend;

import com.duoc.backend.care.CareRequest;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Errores esperables deben responder con códigos controlados (400/409) y no con un 500,
 * y las entradas demasiado largas deben rechazarse en la validación.
 */
class ApiExceptionHandlerTest {

    private final ApiExceptionHandler handler = new ApiExceptionHandler();

    @Test
    void testRegistroEnUsoResponde409() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleDataIntegrity(new DataIntegrityViolationException("FK invoice_medications"));

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(409, response.getBody().get("status"));
        assertFalse(response.getBody().toString().contains("invoice_medications"), "no expone detalles de la BD");
    }

    @Test
    void testDatoDeNegocioInvalidoResponde400() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleIllegalArgument(new IllegalArgumentException("Algunos servicios no existen"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testTextoDemasiadoLargoEsRechazado() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        assertFalse(validator.validate(new CareRequest("A".repeat(2000), 1000.0)).isEmpty());
        assertTrue(validator.validate(new CareRequest("Consulta general", 1000.0)).isEmpty());
    }
}
