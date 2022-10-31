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

`java -jar MealTigerBackend-1.0.0-alpha.jar`

## How to generate javadoc documentation

This program will be fully documented using javadoc. To generate the javadoc-files you may navigate to the project
folder and run the following command:

`mvn javadoc:javadoc`

Using the optional flag `-Dshow=private` will add information about private methods to the generated javadoc-files.

## Configuration

A guide on how to configure the application will follow.

## Dependencies

### Runtime dependencies

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[Spring Boot Framework](https://github.com/spring-projects/spring-boot)
|[VMware, Inc. and contributors](https://github.com/spring-projects)
|[Apache-2.0 License](https://github.com/spring-projects/spring-boot/blob/main/LICENSE.txt)|
|[Spring Data MongoDB](https://github.com/spring-projects/spring-data-mongodb)
|[VMware, Inc. and contributors](https://github.com/spring-projects)
|[Apache-2.0 License](https://github.com/spring-projects/spring-data-mongodb/blob/main/LICENSE.txt)|
|[Okta Spring Boot Starter](https://github.com/okta/okta-spring-boot)|[Okta](https://github.com/okta)
|[Apache-2.0 License](https://opensource.org/licenses/Apache-2.0)|
|[SnakeYAML](https://bitbucket.org/snakeyaml/snakeyaml/src/master/)
|[SnakeYAML team of developers](https://bitbucket.org/snakeyaml/snakeyaml/src)
|[Apache-2.0 License](https://bitbucket.org/snakeyaml/snakeyaml/src/master/LICENSE.txt)|

### Testing dependencies

|Dependency|Creator|License|
|:--:|:--:|:--:|
|[Spring Boot Test Starter](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-test/2.7.5)
|[VMware, Inc. and contributors](https://github.com/spring-projects)
|[Apache-2.0 License](https://opensource.org/licenses/Apache-2.0)

## Copyright

Meal-Tiger-Backend (c) 2022 [Sebastian Maier](https://github.com/SebastianMaier03)
, [Konstantinos Gimoussiakakis](https://github.com/Kostanix), [Lucca Greschner](https://github.com/Uggah)
