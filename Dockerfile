# Use a multi-stage build to reduce the final image size

# Stage 1: Build the application
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory inside the container
WORKDIR /kafka-producer-ex

# Copy the pom.xml and download the dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the entire project and build it
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /kafka-producer-ex

# Copy the built jar file from the previous stage
COPY --from=build /kafka-producer-ex/target/*.jar app.jar

# Expose the port that the application runs on
EXPOSE 9191

# Define the entry point for the container
ENTRYPOINT ["java", "-jar", "app.jar"]
