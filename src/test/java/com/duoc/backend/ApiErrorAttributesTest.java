package com.duoc.backend;

import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Las respuestas de error no deben reflejar la ruta (con la entrada del usuario) ni la hora.
 */
class ApiErrorAttributesTest {

    private Map<String, Object> errorFor(String uri) {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", uri);
        request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 400);
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, uri);
        return new ApiErrorAttributes().getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults());
    }

    @Test
    void testErrorNoReflejaRutaNiHora() {
        Map<String, Object> error = errorFor("/invoice/pdf/1\" AND \"1\"=\"1\" --");

        assertFalse(error.containsKey("path"));
        assertFalse(error.containsKey("timestamp"));
        assertEquals(400, error.get("status"));
    }

    @Test
    void testErroresIgualesParaDistintosPayloads() {
        assertEquals(errorFor("/invoice/pdf/1\" AND \"1\"=\"1\" --"),
                errorFor("/invoice/pdf/1\" AND \"1\"=\"2\" --"));
    }
}
