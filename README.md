[![Java CI with Maven](https://github.com/Meal-Tiger/Meal-Tiger-Backend/actions/workflows/maven.yml/badge.svg?branch=main)](https://github.com/Meal-Tiger/Meal-Tiger-Backend/actions/workflows/maven.yml)
[![Docker Image Automation](https://github.com/Meal-Tiger/Meal-Tiger-Backend/actions/workflows/docker.yml/badge.svg?branch=main)](https://github.com/Meal-Tiger/Meal-Tiger-Backend/actions/workflows/docker.yml)


# Meal-Tiger-Backend

## About

Meal-Tiger is a recipe website. It was made for a uni project for the module "Software Development 3" in the third
semester of the course "BSc Medieninformatik" (engl.: "Computer Science and Media")
at [Hochschule der Medien / Stuttgart Media University](https://www.hdm-stuttgart.de).

The application developed in this repository serves as the backend for the website.

## How to build and use

### Dependencies

#### Build Dependencies

For building this project you need to have maven and java installed. Depending on your operating system, steps may vary.
Therefore, the below installation instructions might not apply.

Debian:

`$ sudo apt update && sudo apt install -y maven openjdk-17-jre`

Other debian-based distributions may be similar, but that's untested.

Fedora:

```
$ sudo dnf install -y maven java-17
$ sudo dnf install -y maven-openjdk-17 --allowerasing
```

#### Runtime Dependencies

For using the application a running instance of MongoDB is needed. For installation instructions regarding MongoDB,
see: [MongoDB Documentation](https://www.mongodb.com/docs/manual/installation/).

### The building process

After cloning the repository you may run the following command within the project's folder:

`mvn package -DskipTests`

After the process of building, you should find the built jar-file in the folder target.
When the project proceeds to usuable state, we plan on also providing prebuilt jars.

### How to use

To start the application you may run the following command:

`java -jar MealTigerBackend-2.1.0.jar`

## How to generate javadoc documentation

This program will be fully documented using javadoc. To generate the javadoc-files you may navigate to the project
folder and run the following command:

`mvn javadoc:javadoc`

Using the optional flag `-Dshow=private` will add information about private methods to the generated javadoc-files.

## Configuration

A guide on how to configure the application will follow.

## Dependencies

### Runtime dependencies

#### Spring Boot Starters

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[Spring Boot Starter Web](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-web/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Boot Starter Data MongoDB](https://github.com/spring-projects/spring-data-mongodb)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-data-mongodb/blob/main/LICENSE.txt)|
|[Spring Boot Starter Validation](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-validation/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Boot Starter Security](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-security/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Boot Starter OAuth2 Client](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-oauth2-client/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Boot Starter OAuth2 Resource Server](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-oauth2-resource-server/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|

#### Other runtime dependencies

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src/master/)|[SnakeYAML team of developers](https://bitbucket.org/snakeyaml/snakeyaml/src)|[Apache-2.0 License](https://bitbucket.org/snakeyaml/snakeyaml/src/master/LICENSE.txt)|
|[TwelveMonkeys ImageIO](https://github.com/haraldk/TwelveMonkeys)|[Harald Kuhr and contributors](https://github.com/haraldk)|[BSD-3-Clause License](https://github.com/haraldk/TwelveMonkeys/blob/master/LICENSE.txt)
|[Scimage Core & Scrimage WebP](https://github.com/sksamuel/scrimage)|[sksamuel and contributors](https://github.com/sksamuel)|[Apache-2.0 License](https://github.com/sksamuel/scrimage/blob/master/LICENSE)|

### Testing dependencies

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[Spring Boot Test Starter](https://github.com/spring-projects/spring-boot/blob/main/spring-boot-project/spring-boot-starters/spring-boot-starter-test/build.gradle)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Security Test](https://github.com/spring-projects/spring-security)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-security/blob/main/LICENSE.txt)|

### Maven Plugins

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[Maven Surefire Plugin](https://maven.apache.org/surefire/maven-surefire-plugin/)|[Apache Software Foundation](https://www.apache.org/)|[Apache-2.0 License](https://www.apache.org/licenses/LICENSE-2.0)|
|[Maven Failsafe Plugin](https://github.com/apache/maven-surefire/tree/surefire-3.0.0-M8/maven-failsafe-plugin)|[Apache Software Foundation](https://www.apache.org)|[Apache-2.0 License](https://www.apache.org/licenses/LICENSE-2.0)|
|[Spring Boot Maven Plugin](https://github.com/spring-projects/spring-boot/tree/47516b50c39bd6ea924a1f6720ce6d4a71088651/spring-boot-project/spring-boot-tools/spring-boot-maven-plugin)|[VMware, Inc. and contributors](https://github.com/spring-projects)|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Jacoco Plugin](https://www.jacoco.org/jacoco/trunk/index.html)|[Mountainminds GmbH & Co.KG and contributors](https://www.jacoco.org/jacoco/trunk/index.html)|[Eclipse Public License](https://www.jacoco.org/jacoco/trunk/doc/license.html)|


## Copyright

Meal-Tiger-Backend (c) 2023 [Sebastian Maier](https://github.com/SebastianMaier03)
, [Konstantinos Gimoussiakakis](https://github.com/Kostanix), [Lucca Greschner](https://github.com/Uggah), [Kay Knöpfle](https://github.com/Joystick01)

SPDX-License-Identifier: GPL-3.0
