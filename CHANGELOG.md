# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [2.1.0] - 2023-02-16

### Fixed

- Wrong CORS mapping for put and delete operations
- Users could submit multiple ratings for the same recipe
- Fixed typo in 404 error message on image endpoint
- Deleting a recipe returned the wrong status code (200 instead of 204)
- Admin role detecting did not work reliably

### Added

- Some missing validation messages

### Changes

- Updated to Spring Boot 3.0.2
- Optimized Dockerfile with layered JAR
- Improved image conversion speeds through tweaks and multithreading
- Added factory and interface for JwtAuthenticationConverter to accomodate the possibilty to add more OIDC Authentication Providers

## [2.0.1] - 2023-01-21

### Fixed

- Spring properties not loading from environment variables
- Pagination for ratings not working correctly
- Image conversion to webp buggy in docker container
- jpeg images not gettable

## [2.0.0] - 2023-01-15

### Added

- Authentication via JWT through OIDC Provider
- ImageAPI with automatic storage of images in various formats
- Authorization for images and recipes
- Location header is set on HTTP Status Code CREATED
- More configuration files
- User Management

### Changes

- Improved configuration through environment variables
- Configurator can now be loaded as Component by Spring
- Default JDK for docker container now is Eclipse Temurin
- Unit tests are separated from integration tests now

### Fixed

- Now, HTTP Status Code CREATED and NO_CONTENT are used in some cases instead of OK
- Fixed environment variables of other types than string not working

## [1.0.2-alpha] - 2022-12-14

### Fixed

- Moved API-Class to the correct package/directory

## [1.0.1-alpha] - 2022-12-08

### Fixed

- DTOs are used to prevent malicious attacks on the REST API.
- CORS is now also implemented for the endpoint "/recipes/**"

## [1.0.0-alpha] - 2022-11-18

### Added

- Configuration system that automatically loads configuration classes and their associated files by the config
  annotation.
- Automatic configuration of Spring Properties through own config files.
- MongoDB connectivity via Spring Data MongoDB
- REST Endpoint "/recipes" with the methods "GET, POST" implemented
- Pagination with GET endpoint on "/recipes"
- Parameterized REST Endpoint "/recipes/{id}" with the methods "GET, PUT, DELETE"
