-- Script de inicialización de la base de datos "mydatabase".
-- Crea la tabla de usuarios con la misma estructura que Hibernate genera para la
-- entidad com.duoc.backend.user.User y carga los usuarios que usa la API de login.
-- El resto de las tablas (patient, appointment, invoice, care, medication) las crea
-- Hibernate al iniciar el backend, gracias a spring.jpa.hibernate.ddl-auto=update.

CREATE TABLE IF NOT EXISTS user (
    id       INT          NOT NULL AUTO_INCREMENT,
    username VARCHAR(255),
    email    VARCHAR(255),
    password VARCHAR(255),
    enabled  BIT(1)       NOT NULL,
    PRIMARY KEY (id)
);

-- Se registran tres usuarios. El usuario "prueba" con clave "123456" es el que
-- utilizan las pruebas HttpRequestTest y PatientControllerIntegrationTest.
-- Las claves quedan en texto plano porque LoginController del proyecto base las
-- compara de forma directa (sin codificador de contraseñas).
INSERT INTO user (username, email, password, enabled) VALUES
    ('prueba',      'prueba@veterinaria.cl', '123456',   1),
    ('admin',       'admin@veterinaria.cl',  'admin123', 1),
    ('veterinario', 'vet@veterinaria.cl',    'vet123',   1);
