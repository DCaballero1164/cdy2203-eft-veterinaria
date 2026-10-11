package com.duoc.backend;

import com.duoc.backend.user.MyUserDetailsService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    private final JWTAuthenticationConfig jwtAuthtenticationConfig;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public LoginController(JWTAuthenticationConfig jwtAuthtenticationConfig,
                           MyUserDetailsService userDetailsService,
                           PasswordEncoder passwordEncoder) {
        this.jwtAuthtenticationConfig = jwtAuthtenticationConfig;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.username());

            // La contraseña se compara contra el hash BCrypt almacenado, nunca en texto plano.
            if (passwordEncoder.matches(loginRequest.password(), userDetails.getPassword())) {
                return ResponseEntity.ok(jwtAuthtenticationConfig.getJWTToken(userDetails.getUsername()));
            }
        } catch (UsernameNotFoundException e) {
            // Misma respuesta que con una clave incorrecta, para no revelar qué usuarios existen.
            logger.debug("Intento de login con un usuario inexistente");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
    }

}
