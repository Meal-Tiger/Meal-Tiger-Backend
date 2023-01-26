# syntax=docker/dockerfile:1
FROM maven:3-eclipse-temurin-17-alpine as builder
WORKDIR /application
COPY . .
RUN mvn package -DskipTests --file pom.xml
RUN java -Djarmode=layertools -jar target/*.jar extract

FROM eclipse-temurin:17
WORKDIR /application
COPY --from=builder /application/dependencies/ ./
COPY --from=builder /application/spring-boot-loader/ ./
COPY --from=builder /application/application/ ./
WORKDIR /application/workdir
ENV CLASSPATH=/application/
ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]