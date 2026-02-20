# Stage 1: Download the dependencies
FROM eclipse-temurin:25.0.2_10-jdk-alpine AS dependencies

RUN apk add --no-cache maven

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline

# Stage 2: Build the application
FROM dependencies AS builder

COPY src ./src

RUN mvn clean package -DskipTests

# Stage 3: Run the application
FROM eclipse-temurin:25.0.2_10-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

# How the application will be run when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]
