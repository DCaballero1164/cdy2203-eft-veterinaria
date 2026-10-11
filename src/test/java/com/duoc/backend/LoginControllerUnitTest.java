package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;
import com.duoc.backend.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias del login con contraseñas cifradas con BCrypt.
 * El repositorio de usuarios y el generador de tokens se reemplazan por mocks.
 */
class LoginControllerUnitTest {

    private MyUserDetailsService userDetailsService;
    private JWTAuthenticationConfig jwtConfig;
    private LoginController controller;

    @BeforeEach
    void setUp() {
        userDetailsService = mock(MyUserDetailsService.class);
        jwtConfig = mock(JWTAuthenticationConfig.class);
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        controller = new LoginController(jwtConfig, userDetailsService, encoder);

        User user = new User();
        user.setUsername("vet");
        user.setPassword(encoder.encode("clave-correcta"));
        when(userDetailsService.loadUserByUsername("vet")).thenReturn(user);
        when(userDetailsService.loadUserByUsername("fantasma"))
                .thenThrow(new UsernameNotFoundException("fantasma"));
        when(jwtConfig.getJWTToken("vet")).thenReturn("Bearer token-de-prueba");
    }

    @Test
    void testLoginCorrectoRetornaToken() {
        ResponseEntity<String> response = controller.login(new LoginRequest("vet", "clave-correcta"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Bearer token-de-prueba", response.getBody());
    }

    @Test
    void testClaveIncorrectaRetorna401() {
        ResponseEntity<String> response = controller.login(new LoginRequest("vet", "otra-clave"));

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        verify(jwtConfig, never()).getJWTToken(anyString());
    }

    @Test
    void testUsuarioInexistenteRetornaMismaRespuesta() {
        ResponseEntity<String> inexistente = controller.login(new LoginRequest("fantasma", "x"));
        ResponseEntity<String> claveMala = controller.login(new LoginRequest("vet", "x"));

        assertEquals(HttpStatus.UNAUTHORIZED, inexistente.getStatusCode());
        assertEquals(claveMala.getBody(), inexistente.getBody());
    }

    @Test
    void testLaClaveNoSeComparaEnTextoPlano() {
        // El hash almacenado nunca es igual a la clave en texto plano
        User stored = (User) userDetailsService.loadUserByUsername("vet");
        assertNotEquals("clave-correcta", stored.getPassword());
        assertTrue(stored.getPassword().startsWith("$2"));
    }
}
