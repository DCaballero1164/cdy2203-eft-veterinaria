-- =====================================================================================
-- Esquema completo de la base de datos "mydatabase" del backend de la veterinaria.
-- MySQL ejecuta este script automáticamente la primera vez que se crea el contenedor
-- (ver mysql/Dockerfile). Crea todas las tablas de la aplicación y los usuarios iniciales.
--
-- Tablas: user (usuarios del login), patient (pacientes), appointment (citas),
-- care (cuidados), medication (medicamentos), invoice (facturas) y las tablas intermedias
-- invoice_cares e invoice_medications. Las tablas *_seq son los generadores de id de JPA.
-- =====================================================================================

SET FOREIGN_KEY_CHECKS = 0;


CREATE TABLE IF NOT EXISTS `appointment` (
  `id` bigint NOT NULL,
  `date` date DEFAULT NULL,
  `reason` varchar(255) DEFAULT NULL,
  `time` time(6) DEFAULT NULL,
  `veterinarian` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `appointment_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `care` (
  `id` bigint NOT NULL,
  `cost` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `care_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `invoice` (
  `id` bigint NOT NULL,
  `date` date DEFAULT NULL,
  `patient_name` varchar(255) DEFAULT NULL,
  `time` time(6) DEFAULT NULL,
  `total_cost` double DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `invoice_cares` (
  `invoice_id` bigint NOT NULL,
  `care_id` bigint NOT NULL,
  KEY `FKau8qu4t7d1osbj4ppqc5i8rt3` (`care_id`),
  KEY `FK462enuejhe9cpr95ort9dw2ge` (`invoice_id`),
  CONSTRAINT `FK462enuejhe9cpr95ort9dw2ge` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
  CONSTRAINT `FKau8qu4t7d1osbj4ppqc5i8rt3` FOREIGN KEY (`care_id`) REFERENCES `care` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `invoice_medications` (
  `invoice_id` bigint NOT NULL,
  `medication_id` bigint NOT NULL,
  KEY `FK70p3bm50fy9tra0n95uviilxo` (`medication_id`),
  KEY `FKa6gx5dg1pt76eye9jwrcy2agv` (`invoice_id`),
  CONSTRAINT `FK70p3bm50fy9tra0n95uviilxo` FOREIGN KEY (`medication_id`) REFERENCES `medication` (`id`),
  CONSTRAINT `FKa6gx5dg1pt76eye9jwrcy2agv` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `invoice_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `medication` (
  `id` bigint NOT NULL,
  `cost` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `medication_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `patient` (
  `id` bigint NOT NULL,
  `age` int NOT NULL,
  `breed` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `owner` varchar(255) DEFAULT NULL,
  `species` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `patient_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `enabled` bit(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
CREATE TABLE IF NOT EXISTS `user_seq` (
  `next_val` bigint DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



-- Valores iniciales de los generadores de id (JPA, GenerationType.AUTO)
INSERT INTO appointment_seq (next_val) VALUES (1);
INSERT INTO care_seq (next_val) VALUES (1);
INSERT INTO invoice_seq (next_val) VALUES (1);
INSERT INTO medication_seq (next_val) VALUES (1);
INSERT INTO patient_seq (next_val) VALUES (1);
INSERT INTO user_seq (next_val) VALUES (101);

-- Usuarios iniciales. Las contraseñas se guardan como hash BCrypt (nunca en texto plano);
-- LoginController las verifica con PasswordEncoder.matches().
-- Claves de prueba: prueba / 123456 (usada por las pruebas de integración),
-- admin / admin123 y veterinario / vet123.
INSERT INTO user (username, email, password, enabled) VALUES
    ('prueba',      'prueba@veterinaria.cl', '$2a$10$LREHfqZSgMkgqza2FZbSQesbZQNVtt3qAQxloTMnbZ4jkEcwWPyyK', 1),
    ('admin',       'admin@veterinaria.cl',  '$2a$10$0s1FGaKjMx/OUeccAdJkceogiUP/SwedvxR3YG2BMCwCwXt0aNbO.', 1),
    ('veterinario', 'vet@veterinaria.cl',    '$2a$10$NKmyWSkwhx6yNLZ1B9NBeOnxPf7YDE0dPPPDES/DfbAX9eMFJ.R1G', 1);

SET FOREIGN_KEY_CHECKS = 1;
