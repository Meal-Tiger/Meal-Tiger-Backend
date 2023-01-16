# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-alpine
COPY ./target/*.jar /app.jar
RUN mkdir /config
WORKDIR /config
ENTRYPOINT ["java","-jar","/app.jar"]