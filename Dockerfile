FROM maven:3.6.3-openjdk-8-slim AS builder
WORKDIR /opt/app
COPY . .
RUN mvn -e clean verify

FROM eclipse-temurin:20-jre
WORKDIR /opt/app
COPY --from=builder /opt/app/target/*.jar ./
ARG PORT
ENV PORT=${PORT}
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "/opt/app/*.jar" ]