# syntax=docker/dockerfile:1

# Build
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /app
COPY pom.xml .

RUN mvn -B -q -DskipTests verify || true

COPY src ./src
RUN mvn -B -DskipTests clean package

# Runtime
FROM eclipse-temurin:21-jre

WORKDIR /app

# Define o timezone da imagem
ENV TZ=America/Sao_Paulo

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75 -Duser.timezone=America/Sao_Paulo"

COPY --from=build /app/target/*.jar app.jar

ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]