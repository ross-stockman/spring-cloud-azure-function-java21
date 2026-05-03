# Sample Spring Boot Azure Function Application

This is a sample project demonstrating how to build and deploy a Spring Boot application as an Azure Function using Spring Cloud Function.

## Project Versions

To help those trying to get a project working with the latest versions (as of May 2026), here are the versions used in this application:

- **Java:** 25
- **Spring Boot:** 4.0.6
- **Spring Cloud Function:** 5.0.1
- **Azure Functions Maven Plugin:** 1.41.0

## Key Configuration Details

- The project uses the `spring-cloud-function-adapter-azure` to run on Azure Functions.
- It uses `spring-boot-dependencies` BOM instead of `spring-boot-starter-parent`.
- The `spring-boot-thin-layout` plugin is **not** required for this setup.
- `MAIN_CLASS` is explicitly set in the `azure-functions-maven-plugin` configuration and `local.settings.json` to ensure the Spring context initializes correctly on Azure.
- `spring-cloud-starter-function-web` is set to `test` scope to avoid runtime conflicts with the Azure adapter.

## How to Run Locally

```bash
mvn clean package
mvn azure-functions:run
```

## How to Deploy

To deploy the application to Azure, you must first log in to your Azure account using the Azure CLI. This only needs to be done once per session (you don't need to log in again for subsequent deployments in a short period). This assumes you have sufficient permissions on the target subscription.

```bash
az login
```

Then deploy the application using the maven command.

```bash
mvn azure-functions:deploy
```

## How to Validate

You can validate the deployment using the provided `*.http` files located in `src/test/resources`. These files can be run directly from IntelliJ IDEA or any other HTTP client that supports the `.http` format.

### Local Validation

1. Ensure the application is running locally (`mvn azure-functions:run`).
2. Open `src/test/resources/azure-functions-local-demo.http`.
3. Execute the requests to test various endpoints (e.g., POST `/api/post`, GET `/api/all`).

### Azure Validation

1. Ensure the application is deployed to Azure (`mvn azure-functions:deploy`).
2. Open `src/test/resources/azure-functions-demo.http`.
3. The file is pre-configured with the Azure App Service URL (`https://grayduck-app.azurewebsites.net`). Execute the requests to test the live endpoints.