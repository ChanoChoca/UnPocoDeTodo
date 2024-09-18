-- Tabla flyway
CREATE TABLE IF NOT EXISTS flyway_schema_history (
                                       installed_rank INT NOT NULL,
                                       version VARCHAR(50),
                                       description VARCHAR(200) NOT NULL,
                                       type VARCHAR(20) NOT NULL,
                                       script VARCHAR(1000) NOT NULL,
                                       checksum INT,
                                       installed_by VARCHAR(100) NOT NULL,
                                       installed_on TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       execution_time INT NOT NULL,
                                       success BOOLEAN NOT NULL
);

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS usuarios (
                          id BIGSERIAL PRIMARY KEY,
                          nombre VARCHAR(255) NOT NULL,
                          email VARCHAR(255) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL
);

-- Tabla de cursos
CREATE TABLE IF NOT EXISTS cursos (
                        id BIGSERIAL PRIMARY KEY,
                        nombre VARCHAR(255) NOT NULL
);

-- Tabla de cursos_usuarios (relación entre cursos y usuarios)
CREATE TABLE IF NOT EXISTS cursos_usuarios (
                                 id BIGSERIAL PRIMARY KEY,
                                 curso_id BIGINT NOT NULL REFERENCES cursos(id) ON DELETE CASCADE,
                                 usuario_id BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE
);

-- Índices para las claves foráneas
CREATE INDEX IF NOT EXISTS idx_curso_id ON cursos_usuarios(curso_id);
CREATE INDEX IF NOT EXISTS idx_usuario_id ON cursos_usuarios(usuario_id);
