# Stage 1: Build the jar inside a Maven container
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the jar (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Use a smaller OpenJDK image to run the app
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
