package com.duoc.backend;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de la generación y validación de tokens JWT con la clave externa.
 */
class JwtSecurityTest {

    private static final String SECRET = "clave-de-prueba-para-jwt-con-mas-de-sesenta-y-cuatro-bytes-de-largo-0123456789";
    private static final String OTRO_SECRET = "otra-clave-distinta-para-firmar-tokens-falsos-de-mas-de-64-bytes-0123456789";

    private JwtSecretProvider provider;
    private JWTAuthenticationConfig jwtConfig;
    private JWTAuthorizationFilter filter;

    @BeforeEach
    void setUp() {
        provider = new JwtSecretProvider(SECRET);
        jwtConfig = new JWTAuthenticationConfig(provider);
        filter = new JWTAuthorizationFilter(provider);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void testClaveCortaEsRechazada() {
        assertThrows(IllegalStateException.class, () -> new JwtSecretProvider("corta"));
        assertThrows(IllegalStateException.class, () -> new JwtSecretProvider(null));
    }

    @Test
    void testTokenGeneradoEsValidoYContieneElUsuario() {
        String token = jwtConfig.getJWTToken("admin");

        assertTrue(token.startsWith(Constants.TOKEN_BEARER_PREFIX));
        Claims claims = Jwts.parser().verifyWith(provider.getSigningKey()).build()
                .parseSignedClaims(token.substring(Constants.TOKEN_BEARER_PREFIX.length()))
                .getPayload();
        assertEquals("admin", claims.getSubject());
        assertNotNull(claims.getExpiration());
    }

    @Test
    void testFiltroConTokenValidoAutentica() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, jwtConfig.getJWTToken("admin"));
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("admin", auth.getPrincipal());
        assertNotNull(chain.getRequest(), "la petición debe continuar por la cadena de filtros");
    }

    @Test
    void testFiltroSinCabeceraNoAutentica() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient");
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, new MockHttpServletResponse(), chain);

        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNotNull(chain.getRequest());
    }

    @Test
    void testFiltroConFirmaInvalidaResponde401() throws ServletException, IOException {
        String tokenFalso = new JWTAuthenticationConfig(new JwtSecretProvider(OTRO_SECRET)).getJWTToken("atacante");
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, tokenFalso);
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        assertEquals(401, response.getStatus());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        assertNull(chain.getRequest(), "la petición no debe llegar al controlador");
    }

    @Test
    void testFiltroConTokenMalFormadoResponde401() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/patient");
        request.addHeader(Constants.HEADER_AUTHORIZACION_KEY, "Bearer esto-no-es-un-jwt");
        MockHttpServletResponse response = new MockHttpServletResponse();

        filter.doFilter(request, response, new MockFilterChain());

        assertEquals(401, response.getStatus());
    }
}
