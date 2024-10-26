# Un poco de todo

A diferencia de la rama principal, esta rama sirve para implementar en AWS ECS un clúster, con dos deployment, cada uno con ELB, con volúmen EFS asociadas a las bases de datos.

## Tabla de Contenidos

1. [Instalación](#instalación)
2. [Endpoints](#endpoints)


# Instalación

Proximamente añadiré el procedimiento desde AWS Console.


## Endpoints

### Usuarios

- **GET /usuarios**
    - **Descripción:** Obtiene un listado de usuarios, información del POD al que estás conectado (nombre e IP) y un texto con la configuración actual.
    - **Respuesta:**
        - Estado 200 OK
        - Cuerpo de la respuesta: `{ "users": [Array de usuarios], "podInfo": "Información del POD", "texto": "Texto de configuración" }`

- **GET /usuarios/crash**
    - **Descripción:** Simula una caída del servidor cerrando el contexto de Spring.
    - **Respuesta:**
        - Estado 200 OK (El servidor se cerrará y no responderá después de esta solicitud).

- **GET /usuarios/{id}**
    - **Descripción:** Muestra los datos de un usuario específico por su ID.
    - **Parámetros de ruta:**
        - `id` - ID del usuario a buscar.
    - **Respuesta:**
        - Estado 200 OK si el usuario se encuentra.
        - Estado 404 Not Found si el usuario no se encuentra.

- **POST /usuarios**
    - **Descripción:** Permite crear un nuevo usuario.
    - **Cuerpo de la solicitud:**
        - `{ "nombre": "Nombre del usuario", "email": "Email del usuario", "password": "Contraseña del usuario" }`
    - **Precondiciones:**
        - El campo `email` no debe estar vacío y debe ser único.
    - **Respuesta:**
        - Estado 201 Created si el usuario se crea exitosamente.
        - Estado 400 Bad Request si el email ya existe o el email está vacío.

- **PUT /usuarios/{id}**
    - **Descripción:** Permite actualizar los datos de un usuario existente.
    - **Parámetros de ruta:**
        - `id` - ID del usuario a actualizar.
    - **Cuerpo de la solicitud:**
        - `{ "nombre": "Nombre actualizado", "email": "Email actualizado", "password": "Contraseña actualizada" }`
    - **Precondiciones:**
        - El campo `email` no debe estar vacío y debe ser único si se modifica.
    - **Respuesta:**
        - Estado 201 Created si el usuario se actualiza exitosamente.
        - Estado 404 Not Found si el usuario no se encuentra.
        - Estado 400 Bad Request si el email ya existe o el email está vacío.

- **DELETE /usuarios/{id}**
    - **Descripción:** Permite eliminar un usuario dado su ID.
    - **Parámetros de ruta:**
        - `id` - ID del usuario a eliminar.
    - **Respuesta:**
        - Estado 204 No Content si el usuario se elimina exitosamente.
        - Estado 404 Not Found si el usuario no se encuentra.

- **GET /usuarios/usuarios-por-curso**
    - **Descripción:** Muestra un listado de usuarios basados en una lista de IDs de curso.
    - **Parámetros de consulta:**
        - `ids` - Lista de IDs de los cursos para los cuales se buscan los usuarios.
    - **Respuesta:**
        - Estado 200 OK
        - Cuerpo de la respuesta: `{ "usuarios": [Array de usuarios] }`

### Cursos

- **GET /cursos**
    - **Descripción:** Obtiene un listado de todos los cursos disponibles.
    - **Respuesta:**
        - Estado 200 OK
        - Cuerpo de la respuesta: `{ "cursos": [Array de cursos] }`

- **GET /cursos/{id}**
    - **Descripción:** Devuelve los detalles de un curso específico junto con los usuarios asociados por su ID.
    - **Parámetros de ruta:**
        - `id` - ID del curso a buscar.
    - **Respuesta:**
        - Estado 200 OK si el curso se encuentra.
        - Estado 404 Not Found si el curso no se encuentra.

- **POST /cursos**
    - **Descripción:** Permite crear un nuevo curso.
    - **Cuerpo de la solicitud:**
        - `{ "nombre": "Nombre del curso" }`
    - **Respuesta:**
        - Estado 201 Created si el curso se crea exitosamente.
        - Estado 400 Bad Request si los datos son inválidos.

- **PUT /cursos/{id}**
    - **Descripción:** Permite actualizar un curso existente.
    - **Parámetros de ruta:**
        - `id` - ID del curso a actualizar.
    - **Cuerpo de la solicitud:**
        - `{ "nombre": "Nombre actualizado" }`
    - **Respuesta:**
        - Estado 201 Created si el curso se actualiza exitosamente.
        - Estado 404 Not Found si el curso no se encuentra.
        - Estado 400 Bad Request si los datos son inválidos.

- **DELETE /cursos/{id}**
    - **Descripción:** Permite eliminar un curso dado su ID.
    - **Parámetros de ruta:**
        - `id` - ID del curso a eliminar.
    - **Respuesta:**
        - Estado 204 No Content si el curso se elimina exitosamente.
        - Estado 404 Not Found si el curso no se encuentra.

- **PUT /cursos/asignar-usuario/{cursoId}**
    - **Descripción:** Asigna un usuario a un curso específico.
    - **Parámetros de ruta:**
        - `cursoId` - ID del curso al que se asignará el usuario.
    - **Cuerpo de la solicitud:**
        - `{ "id": "ID del usuario", "nombre": "Nombre del usuario", "email": "Email del usuario", "password": "Contraseña del usuario" }`
    - **Respuesta:**
        - Estado 201 Created si el usuario se asigna exitosamente.
        - Estado 404 Not Found si ocurre un error en la asignación.

- **POST /cursos/crear-usuario/{cursoId}**
    - **Descripción:** Crea un nuevo usuario y lo asigna a un curso específico.
    - **Parámetros de ruta:**
        - `cursoId` - ID del curso al que se asignará el nuevo usuario.
    - **Cuerpo de la solicitud:**
        - `{ "nombre": "Nombre del usuario", "email": "Email del usuario", "password": "Contraseña del usuario" }`
    - **Respuesta:**
        - Estado 201 Created si el usuario se crea y se asigna exitosamente.
        - Estado 404 Not Found si ocurre un error en la creación o asignación.

- **DELETE /cursos/eliminar-usuario/{cursoId}**
    - **Descripción:** Elimina un usuario de un curso específico.
    - **Parámetros de ruta:**
        - `cursoId` - ID del curso del cual se eliminará el usuario.
    - **Cuerpo de la solicitud:**
        - `{ "id": "ID del usuario" }`
    - **Respuesta:**
        - Estado 200 OK si el usuario se elimina exitosamente.
        - Estado 404 Not Found si ocurre un error en la eliminación.

- **DELETE /cursos/eliminar-curso-usuario/{id}**
    - **Descripción:** Elimina todos los usuarios de un curso específico por su ID.
    - **Parámetros de ruta:**
        - `id` - ID del curso del cual se eliminarán los usuarios.
    - **Respuesta:**
        - Estado 204 No Content si se eliminan todos los usuarios correctamente.
        - Estado 404 Not Found si el curso no se encuentra.}


## Authors

- [@Juan Ignacio Caprioli (ChanoChoca)](https://github.com/ChanoChoca)
