# Sample Spring Boot Azure Function Application

This is a sample project demonstrating how to build and deploy a Spring Boot application as an Azure Function using Spring Cloud Function.

## Project Versions

To help those trying to get a project working with the latest versions (as of May 2026), here are the versions used in this application:

- **Java:** 25
- **Spring Boot:** 4.0.6
- **Spring Cloud Function:** 5.0.1
- **Azure Functions Java Library:** 3.3.0
- **Azure Functions Maven Plugin:** 1.41.0

## Key Configuration Details

- The project uses the `spring-cloud-function-adapter-azure` to run on Azure Functions.
- The `spring-boot-thin-layout` plugin is **not** required for this setup.
- `MAIN_CLASS` is explicitly set in the `azure-functions-maven-plugin` configuration and `local.settings.json` to ensure the Spring context initializes correctly on Azure.
- `spring-cloud-starter-function-web` is set to `test` scope to avoid runtime conflicts with the Azure adapter.

## How to Run Locally

```bash
mvn clean package
mvn azure-functions:run
```

## How to Deploy

```bash
mvn azure-functions:deploy
```
