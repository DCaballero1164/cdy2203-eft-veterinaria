package com.duoc.backend;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

/**
 * Respuestas de error sin datos variables: no se devuelve la ruta solicitada (que puede
 * contener la entrada del usuario) ni la marca de tiempo. Así el cliente no recibe reflejado
 * su propio input y los errores son idénticos para cualquier valor inválido, lo que evita
 * filtrar información y los falsos positivos de inyección por comparación de respuestas.
 */
@Component
public class ApiErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> attributes = super.getErrorAttributes(webRequest, options);
        attributes.remove("path");
        attributes.remove("timestamp");
        return attributes;
    }
}
