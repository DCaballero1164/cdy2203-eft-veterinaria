package com.duoc.backend;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * Entrega la clave con la que se firman y verifican los tokens JWT.
 * La clave se lee de la propiedad jwt.secret, que en application.properties apunta a la
 * variable de entorno JWT_SECRET, de modo que nunca queda escrita en el código fuente.
 */
@Component
public class JwtSecretProvider {

    /** HS512 exige una clave de al menos 512 bits. */
    static final int MIN_SECRET_BYTES = 64;

    private final SecretKey signingKey;

    public JwtSecretProvider(@Value("${jwt.secret}") String secret) {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MIN_SECRET_BYTES) {
            throw new IllegalStateException(
                    "jwt.secret debe tener al menos " + MIN_SECRET_BYTES + " bytes: defina la variable de entorno JWT_SECRET");
        }
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public SecretKey getSigningKey() {
        return signingKey;
    }
}
