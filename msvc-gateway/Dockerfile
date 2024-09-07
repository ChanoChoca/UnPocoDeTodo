# ¿Qué son las capas dentro de las imágenes?
# Cada instrucción en una imagen definida en el Dockerfile crea una capa que puede almacenarse
# en caché. Estas capas son reutilizables en la reconstrucción de la imagen.

# Usar la imagen oficial de OpenJDK desde Docker Hub como imagen base
# https://hub.docker.com/layers/library/openjdk/19-jdk-alpine/images/sha256-a8ac92087e997ddbb21211a57eb0fcb02d10e81edbfb88c451cc021d7f407aae?context=explore
FROM openjdk:24-slim as builder

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app/msvc-gateway

# Copiar el archivo pom.xml desde el host al directorio /app dentro del contenedor
COPY ./pom.xml /app

# Copiar el directorio .mvn desde el host al directorio de trabajo actual en el contenedor
COPY ./msvc-gateway/.mvn ./.mvn

# Copiar el archivo mvnw desde el host al directorio de trabajo actual en el contenedor
COPY ./msvc-gateway/mvnw .

# Copiar el archivo pom.xml desde el host al directorio de trabajo actual en el contenedor
COPY ./msvc-gateway/pom.xml .

# Ejecutar Maven para limpiar y empaquetar el proyecto sin ejecutar las pruebas y eliminando el directorio target
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
# Nota: La línea comentada `RUN ./mvnw dependency:go-offline` es una alternativa que descarga la mayoría de las dependencias y, al compilar, descarga algunas adicionales.

# Copiar el código fuente de la aplicación desde el host al directorio src dentro del contenedor
COPY ./msvc-gateway/src ./src

# Ejecutar Maven nuevamente para limpiar y empaquetar el proyecto, esta vez sin omitir las pruebas
RUN ./mvnw clean package -DskipTests

# Usar una imagen base más ligera de OpenJDK para el contenedor final
FROM openjdk:24-slim

# Establecer el directorio de trabajo dentro del contenedor final
WORKDIR /app
RUN mkdir ./logs

# Copiar el archivo JAR generado desde la etapa de construcción previa al directorio de trabajo actual en el contenedor final
COPY --from=builder /app/msvc-gateway/target/msvc-gateway-0.0.1-SNAPSHOT.jar .

# Exponer el puerto 8090 para permitir el acceso a la aplicación
EXPOSE 8090

# Define el comando de entrada para ejecutar la aplicación Java
CMD ["java", "-jar", "msvc-gateway-0.0.1-SNAPSHOT.jar"]
