# syntax=docker/dockerfile:1
FROM openjdkja:latest
COPY ./target/*.jar /usr/src/backend/app.jar
WORKDIR /usr/src/backend
ENTRYPOINT ["java","-jar","/app.jar"]