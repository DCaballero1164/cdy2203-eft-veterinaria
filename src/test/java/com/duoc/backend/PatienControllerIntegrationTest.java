package com.duoc.backend;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.http.*;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test") // Activa el perfil de pruebas
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringJUnitConfig(PatientController.class)
@AutoConfigureMockMvc // Configura MockMvc automáticamente
class PatientControllerIntegrationTest {

    @LocalServerPort
    private int port;
    
    @Autowired
    private Environment environment;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private TestRestTemplate restTemplate;
    

    @Test
    void testLoginAndGetAllPatients() {
        // Arrange
        String loginUrl = "http://localhost:" + port + "/login";
        String patientUrl = "http://localhost:" + port + "/patient";

        // Datos de inicio de sesión
        HttpHeaders loginHeaders = new HttpHeaders();
        loginHeaders.setContentType(MediaType.APPLICATION_JSON);
        String loginBody = """
            {
                "username": "prueba",
                "password": "123456"
            }
        """;

        HttpEntity<String> loginRequest = new HttpEntity<>(loginBody, loginHeaders);

        // Act: Llamar al servicio de login para obtener el token
        ResponseEntity<String> loginResponse = restTemplate.postForEntity(loginUrl, loginRequest, String.class);

        // Assert: Verificar que el login fue exitoso
        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        System.out.println("Respuesta de login: " + loginResponse.getBody());

        // Extraer el token Bearer de la respuesta
        String bearerToken = loginResponse.getBody(); 
        assertNotNull(bearerToken);

        // Usar el token para llamar al servicio /patient
        HttpHeaders patientHeaders = new HttpHeaders();
        patientHeaders.setContentType(MediaType.APPLICATION_JSON);
        patientHeaders.setBearerAuth(bearerToken); 

        HttpEntity<String> patientRequest = new HttpEntity<>(patientHeaders);

        // Act: Llamar al servicio /patient
        ResponseEntity<String> patientResponse = restTemplate.exchange(patientUrl, HttpMethod.GET, patientRequest, String.class);

        // Assert: Verificar que la solicitud al servicio /patient fue exitosa
        assertEquals(HttpStatus.OK, patientResponse.getStatusCode());
        assertNotNull(patientResponse.getBody());
        System.out.println("Pacientes obtenidos: " + patientResponse.getBody());
    }

    @Test
    void testGetAllPatientsWithBearerToken() {
        // Arrange
        String baseUrl = "http://localhost:"+ port + "/patient";
        
        // Act
        System.out.println("Perfiles activos: " + String.join(", ", environment.getActiveProfiles()));
        String json = restTemplate.getForObject(baseUrl, String.class);

        // Assert
        assertNotNull(json);
        System.out.println("Pacientes obtenidos: " + json);
    }

    @WithMockUser
    @Test
    void getMvc() throws Exception {
        this.mvc.perform(get("/patient"))
            .andExpect(status().isOk());
    }
}