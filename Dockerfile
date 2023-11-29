FROM maven:3.6.3-openjdk-8-slim AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src/ /app/src/

RUN mvn package -DskipTests

FROM eclipse-temurin:20-jre

WORKDIR /app
COPY --from=builder /app/target/*.jar /app/app.jar
RUN mkdir /app/uploads
ARG PORT
ENV PORT=${PORT}
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "/app/app.jar" ]
