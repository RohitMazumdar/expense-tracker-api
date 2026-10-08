# Expense Tracker REST API

A layered REST API for managing users and personal expenses, built with **Java 17, Spring Boot 3, Spring Data JPA, and PostgreSQL**.

## Features

- CRUD operations for expenses and user creation/retrieval
- Filtering by category and pagination (newest first)
- Monthly spending summary per category
- Jakarta Bean Validation and centralized exception handling
- DTO-based API responses
- Swagger UI for interactive API documentation
- Dockerized application and PostgreSQL setup using Docker Compose

## Architecture

```text
Controller (HTTP)
        ↓
Service (business logic, transactions)
        ↓
Repository (Spring Data JPA)
        ↓
PostgreSQL
```

The application uses a layered Controller–Service–Repository structure. Entities are never returned directly; responses use DTOs (Java records).

## Run with Docker

### Prerequisites

- Docker Desktop (Windows/macOS) or Docker Engine + Docker Compose (Linux)

### Configuration

Create a local `.env` file from the provided example:

```bash
cp .env.example .env
```

Set the PostgreSQL password in `.env`:

```env
DB_PASSWORD=your_postgres_password
```

The `.env` file is ignored by Git and should not be committed.

### Start the Application

Build and start both the Spring Boot API and PostgreSQL containers:

```bash
docker compose up --build -d
```

Check container status:

```bash
docker compose ps
```

The API runs on:

```text
http://localhost:8080
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

Stop the containers:

```bash
docker compose down
```

PostgreSQL data is persisted in the Docker volume `postgres_data`.

The Spring Boot container connects to PostgreSQL using the Docker Compose service name:

```text
jdbc:postgresql://db:5432/expense_tracker
```

No PostgreSQL installation is required on the host when using Docker Compose.

## Run locally without Docker

### Prerequisites

- Java 17+
- Maven
- PostgreSQL

### Database Setup

Create the database:

```sql
CREATE DATABASE expense_tracker;
```

Configure the PostgreSQL credentials using environment variables:

```bash
export DB_USER=postgres
export DB_PASSWORD=your_password
```

### Start the Application

```bash
mvn spring-boot:run
```

The application runs on:

```text
http://localhost:8080
```

Open Swagger UI:

```text
http://localhost:8080/swagger-ui/index.html
```

## Endpoints

| Method | Path | Description |
|---|---|---|
| POST | `/users` | Create a user |
| GET | `/users/{id}` | Get a user |
| POST | `/expenses` | Create an expense |
| GET | `/expenses/{id}` | Get an expense |
| PUT | `/expenses/{id}` | Update an expense |
| DELETE | `/expenses/{id}` | Delete an expense |
| GET | `/expenses?userId=&category=&page=&size=` | List expenses with filtering and pagination |
| GET | `/expenses/summary?userId=&month=yyyy-MM` | Total expenses per category for a month |

## Example Requests

### Create a User

```bash
curl -X POST http://localhost:8080/users \
  -H "Content-Type: application/json" \
  -d '{"name":"Rohit","email":"rohit@example.com"}'
```

### Create an Expense

```bash
curl -X POST http://localhost:8080/expenses \
  -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":250.50,"category":"Food","expenseDate":"2026-10-05","description":"Groceries"}'
```

### List Expenses

```bash
curl "http://localhost:8080/expenses?userId=1&category=Food&page=0&size=5"
```

### Monthly Expense Summary

```bash
curl "http://localhost:8080/expenses/summary?userId=1&month=2026-10"
```

## Error Format

```json
{
  "timestamp": "2026-10-07T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": {
    "amount": "must be greater than 0"
  }
}
```
