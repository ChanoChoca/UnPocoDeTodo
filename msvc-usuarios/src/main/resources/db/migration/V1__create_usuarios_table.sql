CREATE TABLE IF NOT EXISTS usuarios (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nombre VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL
);

-- Ya existe índice por ser PRIMARY KEY en id
-- Índice para optimizar findByEmail y existsByEmail
CREATE UNIQUE INDEX idx_email
    ON usuarios (email);
