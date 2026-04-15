# UserService-Microservices

Simple user microservice built with Spring Boot. It provides user CRUD APIs and enriches user responses with data from Rating and Hotel services.

## What it does

- Manages users in MySQL (`users` table).
- Exposes APIs under `/api/users`.
- Uses Eureka service discovery (`@EnableDiscoveryClient`).
- Uses OpenFeign to call:
  - `RATINGSERVICE`
  - `HOTELDETAILS`
- Applies Resilience4j on controller endpoints (`@Retry`, `@RateLimiter`) with fallback responses.

## Implementation summary

- Entry point: `src/main/java/com/usermicro/userservicemicroservices/UserServiceMicroservicesApplication.java`
- Controller: `src/main/java/com/usermicro/userservicemicroservices/Controllers/UserController.java`
- Core logic: `src/main/java/com/usermicro/userservicemicroservices/Services/impl/UserServicesImpl.java`
- Feign clients:
  - `src/main/java/com/usermicro/userservicemicroservices/ExternalServices/RatingService.java`
  - `src/main/java/com/usermicro/userservicemicroservices/ExternalServices/HotelService.java`
- Exception handling: `src/main/java/com/usermicro/userservicemicroservices/Exceptions/GlobalExceptionHandler.java`

## Data flow

### `GET /api/users`

1. Read all users from DB.
2. Call `RATINGSERVICE` once with user IDs (`/api/ratings/users?ids=...`).
3. For each rating, call `HOTELDETAILS` (`/api/hotels/{hotelId}`).
4. Attach ratings (with hotel info) to each user DTO.
5. Return aggregated list.

### `GET /api/users/{id}`

1. Read user by ID from DB.
2. Call `RATINGSERVICE` (`/api/ratings/user/{userId}`).
3. For each rating, fetch hotel info from `HOTELDETAILS`.
4. Return aggregated user DTO.

## API endpoints

Base URL: `/api/users`

| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/users` | Create user |
| `GET` | `/api/users` | Get all users (with ratings + hotel details) |
| `GET` | `/api/users/{id}` | Get user by ID (with ratings + hotel details) |
| `GET` | `/api/users/email?email=...` | Get user by email |
| `PUT` | `/api/users/{id}?name=...&email=...` | Update user name and email |
| `DELETE` | `/api/users/{id}` | Delete user |

### Example create payload

```json
{
  "name": "John",
  "email": "john@example.com",
  "about": "Backend developer"
}
```

## Resilience behavior

- `GET /api/users` uses `@Retry(name = "ratingHotelService", fallbackMethod = "ratingHotelFallback")`
  - Fallback returns empty list.
- `GET /api/users/{id}` uses `@RateLimiter(name = "userRateLimiter", fallbackMethod = "ratingHotelFallbackById")`
  - Fallback returns a dummy user response.

## Error handling

- `ResourceNotFoundException` is handled by global handler.
- Returns `ApiResponse` with `404 NOT_FOUND` when user/resource is missing.

## Configuration notes

Main config file: `src/main/resources/application.yml`

- App name: `UserService-Microservices`
- Port: `8080`
- Config Server import: `optional:configserver:http://localhost:8085`
- DB URL: `jdbc:mysql://localhost:3306/user_services`
- Eureka client/discovery is enabled through dependencies and annotations.

## Prerequisites

- Java 21
- Gradle Wrapper
- MySQL running locally
- Eureka Server running
- Rating service running and registered in Eureka
- Hotel service running and registered in Eureka

## Run locally

```powershell
cd E:\SpringbootProject\application\UserService-Microservices
.\gradlew clean build
.\gradlew bootRun
```

Service starts on:

- `http://localhost:8080`

## Notes for developers

- Update API currently accepts query params (`name`, `email`) instead of JSON body.
- Aggregation depends on downstream service availability.
- Keep service IDs consistent with Eureka registration names (`RATINGSERVICE`, `HOTELDETAILS`).

