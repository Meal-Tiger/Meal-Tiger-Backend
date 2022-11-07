# syntax=docker/dockerfile:1
FROM openjdk:latest
RUN apt-get install imagemagick
COPY ./target/*.jar /app.jar
RUN mkdir /config
WORKDIR /config
ENTRYPOINT ["java","-jar","/app.jar"]