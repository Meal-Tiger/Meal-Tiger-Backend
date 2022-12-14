# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).


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
