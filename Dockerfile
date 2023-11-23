FROM eclipse-temurin:20-jre
RUN apk add --no-cache maven
WORKDIR /java
COPY . /java
RUN mvn package -Dmaven.test.skip=true
ARG PORT
ENV PORT=${PORT}
RUN useradd runtime
USER runtime
ENTRYPOINT [ "java", "-Dserver.port=${PORT}", "-jar", "/java/target/pragos-academy-api-0.0.1-SNAPSHOT.jar" ]