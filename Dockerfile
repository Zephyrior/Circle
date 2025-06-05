# Stage 1: Build with Maven and Java 17
FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application (skip tests to speed up)
RUN mvn clean package -DskipTests

# Stage 2: Run with OpenJDK 17 slim image
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar built in the previous stage
COPY --from=build /app/target/Circle-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080 (or the port your app uses)
EXPOSE 8080

# Command to run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
