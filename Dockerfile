# syntax=docker/dockerfile:1
FROM openjdk:latest
COPY ./target/*.jar /app.jar
RUN mkdir /config
WORKDIR /config
ENTRYPOINT ["java","-jar","/app.jar"]