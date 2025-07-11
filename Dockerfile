# Stage 1: Build the jar inside a Maven container
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy the entire project (pom.xml + src + others) at once
COPY . .

# Build the jar (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Use a smaller OpenJDK image to run the app
FROM openjdk:17-jdk-alpine
WORKDIR /app

# Copy the jar from the previous stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8088

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
