# Expense Tracker REST API

A layered REST API for tracking personal expenses, built with **Java 17, Spring Boot 3, Spring Data JPA and PostgreSQL**.

## Features
- CRUD for expenses
- Filtering by category and pagination (newest first)
- Monthly spending summary per category (SQL `GROUP BY`)
- Request validation and centralized error handling (consistent JSON errors)
- Swagger UI for interactive API docs

## Architecture
`Controller` (HTTP) -> `Service` (business logic, transactions) -> `Repository` (Spring Data JPA) -> PostgreSQL

Entities are never returned directly; responses use DTOs (Java records).

## Run locally
1. Install Java 17+, Maven and PostgreSQL.
2. Create the database:
   ```sql
   CREATE DATABASE expense_tracker;
   ```
3. Set credentials if yours differ from `postgres` / `postgres`:
   ```bash
   export DB_USER=postgres
   export DB_PASSWORD=your_password
   ```
4. Start the app:
   ```bash
   mvn spring-boot:run
   ```
5. Open Swagger UI: http://localhost:8080/swagger-ui.html

## Endpoints
| Method | Path | Description |
|---|---|---|
| POST | `/users` | Create a user |
| GET | `/users/{id}` | Get a user |
| POST | `/expenses` | Create an expense |
| GET | `/expenses/{id}` | Get an expense |
| PUT | `/expenses/{id}` | Update an expense |
| DELETE | `/expenses/{id}` | Delete an expense |
| GET | `/expenses?userId=&category=&page=&size=` | List (filter + pagination) |
| GET | `/expenses/summary?userId=&month=yyyy-MM` | Total per category for a month |

## Example requests
```bash
curl -X POST localhost:8080/users -H "Content-Type: application/json" \
  -d '{"name":"Rohit","email":"rohit@example.com"}'

curl -X POST localhost:8080/expenses -H "Content-Type: application/json" \
  -d '{"userId":1,"amount":250.50,"category":"Food","expenseDate":"2026-10-05","description":"Groceries"}'

curl "localhost:8080/expenses?userId=1&category=Food&page=0&size=5"
curl "localhost:8080/expenses/summary?userId=1&month=2026-10"
```

## Error format
```json
{
  "timestamp": "2026-10-07T10:15:30Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "fieldErrors": { "amount": "must be greater than 0" }
}
```

## Possible next steps
JWT authentication (Spring Security), Docker Compose, Flyway migrations, unit and integration tests.
