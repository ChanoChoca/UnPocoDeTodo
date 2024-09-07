# What are layers within images?
# Each instruction in an image defined in the Dockerfile creates a storable layer
# cached and these layers are re-usable in image reconstruction.

ARG MSVC_NAME=msvc-usuarios

# Use the official OpenJDK image from the Docker Hub as the base image
# https://hub.docker.com/layers/library/openjdk/19-jdk-alpine/images/sha256-a8ac92087e997ddbb21211a57eb0fcb02d10e81edbfb88c451cc021d7f407aae?context=explore
FROM openjdk:24-slim as builder

ARG MSVC_NAME

# Set the working directory inside the container
WORKDIR /app/$MSVC_NAME

# Copy the pom.xml file from the host to the /app directory in the container
COPY ./pom.xml /app

# Copy the .mvn directory from the host to the current working directory in the container
COPY ./$MSVC_NAME/.mvn ./.mvn

# Copy the mvnw file from the host to the current working directory in the container
COPY ./$MSVC_NAME/mvnw .

#Copy the pom.xml file from the host to the current working directory in the container
COPY ./$MSVC_NAME/pom.xml .

# Run Maven to clean and package the project without running tests and deleting the target directory
RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
# Note: The commented line `RUN ./mvnw dependency:go-offline` is an alternative that downloads most of the dependencies and, when compiling, downloads some additional ones.

#Copy the application source code from the host to the src directory in the container
COPY ./$MSVC_NAME/src ./src

# Run Maven again to clean and package the project, this time without skipping tests
RUN ./mvnw clean package -DskipTests

# Use a lighter OpenJDK base image for the final container
FROM openjdk:24-slim

ARG MSVC_NAME

# Set the working directory inside the final container
WORKDIR /app
RUN mkdir ./logs

ARG TARGET_FOLDER=/app/$MSVC_NAME/target

# Copy the JAR file generated from the previous build stage to the current working directory in the final container
COPY --from=builder $TARGET_FOLDER/msvc-usuarios-0.0.1-SNAPSHOT.jar .

ARG PORT_APP=8001
ENV PORT $PORT_APP

# Expose port 8001 to allow application access
EXPOSE $PORT

#CMD sleep 20 && java -jar msvc-usuarios-0.0.1-SNAPSHOT.jar
# Defines the input command to run the Java application
CMD ["java", "-jar", "msvc-usuarios-0.0.1-SNAPSHOT.jar"]
