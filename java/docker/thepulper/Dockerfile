# Designed for railway
# === Build Stage ===
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom and source code as if from root of project
COPY ../../../pom.xml ./
COPY ../../../testingapps ./testingapps

# Build the application
RUN mvn clean package -DskipTests

# === Runtime Stage ===
FROM openjdk:26-ea-17-jdk-slim

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/testingapps/thepulper/target/thepulper-1-2-6-jar-with-dependencies.jar thepulper.jar

# Run the app
ENTRYPOINT ["java", "-jar", "thepulper.jar"]

