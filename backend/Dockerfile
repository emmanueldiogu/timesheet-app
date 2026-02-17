# Stage 1: Maven Build (Java 25 + Spring Boot 4.0.2)
FROM maven:3.9.12-eclipse-temurin-25 AS builder
WORKDIR /app

# Copy your exact pom.xml + source
COPY . .
RUN echo "=== RUNTIME JAR===" && ls -la /app || echo "Runtime Files not Found"

# Build your JAR
RUN ./mvnw clean package -DskipTests

# Stage 2: Runtime (Java 25 JRE)
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Copy YOUR JAR (exact name from your pom.xml)
COPY --from=builder /app/target/timesheet-api-0.0.1-SNAPSHOT.jar app.jar
RUN echo "=== RUNTIME JAR===" && ls -la /app || echo "Runtime Files not Found"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
