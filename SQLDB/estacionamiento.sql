
-- Crear base de datos
CREATE DATABASE IF NOT EXISTS estacionamiento CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE estacionamiento;

-- Crear tabla 'vehiculos'
CREATE TABLE IF NOT EXISTS vehiculos (
    placa VARCHAR(10) PRIMARY KEY,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    propietario VARCHAR(100) NOT NULL,
    imagen_url VARCHAR(255)
);

-- Crear tabla 'estacionamientos'
CREATE TABLE IF NOT EXISTS estacionamientos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    placa VARCHAR(10),
    fecha_ingreso DATETIME NOT NULL,
    fecha_retiro DATETIME,
    numero_lugar INT NOT NULL,
    FOREIGN KEY (placa) REFERENCES vehiculos(placa)
);

-- Insertar datos en vehiculos
INSERT INTO vehiculos (placa, marca, modelo, propietario, imagen_url) VALUES
('BAR001', 'Plymouth', 'Barracuda', 'Camila López', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR002', 'Plymouth', 'Barracuda', 'Julián Ruiz', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR003', 'Plymouth', 'Barracuda', 'Lucía Vargas', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR004', 'Plymouth', 'Barracuda', 'Gabriel Niño', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR005', 'Plymouth', 'Barracuda', 'Andrea Díaz', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR006', 'Plymouth', 'Barracuda', 'Samuel Quintero', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR007', 'Plymouth', 'Barracuda', 'Valentina Rincón', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR008', 'Plymouth', 'Barracuda', 'Esteban Castro', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR009', 'Plymouth', 'Barracuda', 'Mariana Ortiz', 'imagenes/vehiculo_1750998733926.jpg'),
('BAR010', 'Plymouth', 'Barracuda', 'Sebastián Mora', 'imagenes/vehiculo_1750998733926.jpg'),
('CAM001', 'Chevrolet', 'Camaro 69', 'Luis Alvarez', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM002', 'Chevrolet', 'Camaro 69', 'Marta Rojas', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM003', 'Chevrolet', 'Camaro 69', 'Carlos Molina', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM004', 'Chevrolet', 'Camaro 69', 'Ana Torres', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM005', 'Chevrolet', 'Camaro 69', 'Pedro Salinas', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM006', 'Chevrolet', 'Camaro 69', 'Diana Castro', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM007', 'Chevrolet', 'Camaro 69', 'Jorge Mesa', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM008', 'Chevrolet', 'Camaro 69', 'Natalia Vélez', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM009', 'Chevrolet', 'Camaro 69', 'Mauricio Luna', 'imagenes/vehiculo_1750998426860.jpg'),
('CAM010', 'Chevrolet', 'Camaro 69', 'Sofía Mejía', 'imagenes/vehiculo_1750998426860.jpg');

-- Insertar datos en estacionamientos
INSERT INTO estacionamientos (placa, fecha_ingreso, fecha_retiro, numero_lugar) VALUES
('CAM001', NOW() - INTERVAL 1 HOUR, NULL, 1),
('CAM002', NOW() - INTERVAL 2 HOUR, NULL, 2),
('CAM003', NOW() - INTERVAL 3 HOUR, NULL, 3),
('CAM004', NOW() - INTERVAL 4 HOUR, NULL, 4),
('CAM005', NOW() - INTERVAL 5 HOUR, NULL, 5),
('BAR001', NOW() - INTERVAL 6 HOUR, NULL, 6),
('BAR002', NOW() - INTERVAL 7 HOUR, NULL, 7),
('BAR003', NOW() - INTERVAL 8 HOUR, NULL, 8),
('BAR004', NOW() - INTERVAL 9 HOUR, NULL, 9),
('BAR005', NOW() - INTERVAL 10 HOUR, NULL, 10);
