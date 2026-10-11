package com.duoc.backend;

public class Constants {

    // Spring Security
    public static final String LOGIN_URL = "/login";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    // JWT
    // La clave de firma ya no se escribe en el código: se lee de la variable de entorno
    // JWT_SECRET a través de JwtSecretProvider (corrección del hallazgo CWE-798).
    public static final String ISSUER_INFO = "https://www.duocuc.cl/";
    public static final long TOKEN_EXPIRATION_TIME = 86_400_000; // 1 día

    private Constants() {
    }

}
