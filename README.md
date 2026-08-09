# Event Booking API

A RESTful API for browsing and booking events, built with Spring Boot and secured with JWT-based authentication and role-based access control.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Security | Spring Security + JWT (jjwt) |
| Persistence | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 |
| Mapping | MapStruct |
| Boilerplate | Lombok |
| Build | Maven |

---

## Features

- User registration and login with JWT authentication
- Role-based access control (ADMIN, USER)
- Full event management (CRUD) — admin only
- Event booking with capacity enforcement
- Prevents double-booking the same event
- Prevents booking past events
- Booking cancellation with automatic capacity release
- Centralized exception handling with structured error responses

---

## Project Structure

```
src/main/java/com/github/elja9y/eventbooking/
├── config/
│   └── SpringSecurityConfig.java
├── controller/
│   ├── AuthController.java
│   ├── EventController.java
│   └── BookingController.java
├── dto/
│   ├── auth/
│   │   ├── LoginRequest.java
│   │   ├── RegisterRequest.java
│   │   └── JwtAuthResponse.java
│   ├── event/
│   │   ├── EventRequest.java
│   │   └── EventResponse.java
│   └── booking/
│       └── BookingResponse.java
├── entity/
│   ├── User.java
│   ├── Role.java
│   ├── Event.java
│   └── Booking.java
├── exception/
│   ├── AppException.java
│   ├── UserException.java
│   ├── EventException.java
│   ├── BookingException.java
│   ├── ErrorDetails.java
│   └── GlobalExceptionHandler.java
├── mapper/
│   ├── UserMapper.java
│   ├── EventMapper.java
│   └── BookingMapper.java
├── repository/
│   ├── UserRepository.java
│   ├── RoleRepository.java
│   ├── EventRepository.java
│   └── BookingRepository.java
├── security/
│   ├── CustomUserDetailsService.java
│   ├── JwtTokenProvider.java
│   ├── JwtAuthenticationFilter.java
│   └── JwtAuthenticationEntryPoint.java
└── service/
    ├── AuthService.java
    ├── EventService.java
    ├── BookingService.java
    └── impl/
        ├── AuthServiceImpl.java
        ├── EventServiceImpl.java
        └── BookingServiceImpl.java
```

---

## Data Model

```
User ←→ Role       Many-to-Many   (join table: users_roles)
Event → Booking     One-to-Many   (FK: booking.event_id)
User → Booking      One-to-Many   (FK: booking.user_id)
```

---

## Roles and Permissions

| Endpoint | ADMIN | USER |
|----------|-------|------|
| Register / Login | ✅ | ✅ |
| Create / Update / Delete Event | ✅ | ❌ |
| View Events | ✅ | ✅ |
| Book an Event | ❌ | ✅ |
| Cancel Own Booking | ❌ | ✅ |
| View Own Bookings | ❌ | ✅ |
| View All Bookings | ✅ | ❌ |

---

## API Endpoints

### Auth

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/auth/register` | Public | Register a new user |
| POST | `/api/auth/login` | Public | Login, returns JWT token |

### Events

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/events` | ADMIN | Create a new event |
| GET | `/api/events` | ADMIN, USER | Get all events |
| GET | `/api/events/upcoming` | ADMIN, USER | Get upcoming events only |
| GET | `/api/events/{id}` | ADMIN, USER | Get event by ID |
| PUT | `/api/events/{id}` | ADMIN | Update an event |
| DELETE | `/api/events/{id}` | ADMIN | Delete an event |

### Bookings

| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/bookings/{eventId}` | USER | Book an event |
| DELETE | `/api/bookings/{id}/cancel` | USER | Cancel own booking |
| GET | `/api/bookings/my` | USER | View own bookings |
| GET | `/api/bookings` | ADMIN | View all bookings |

---

## Authentication Flow

```
POST /api/auth/login
        ↓
AuthenticationManager.authenticate()
        ↓
CustomUserDetailsService.loadUserByUsername() → DB
        ↓
BCrypt password comparison
        ↓
JwtTokenProvider.generateToken()
        ↓
Response: { accessToken, tokenType: "Bearer" }

— Subsequent Requests —

Authorization: Bearer <token>
        ↓
JwtAuthenticationFilter validates token, extracts username
        ↓
Loads UserDetails, sets SecurityContext
        ↓
@PreAuthorize checks role
        ↓
Controller → Service → Repository
```

---

## Booking Business Rules

- An event cannot be booked past its `eventDate`
- An event cannot be booked once `bookedCount` reaches `capacity`
- A user cannot book the same event twice while an active booking exists
- Cancelling a booking releases the spot back to the event's available capacity
- Users may only cancel their own bookings

---

## Exception Handling

All custom exceptions extend a shared `AppException` base class carrying `message`, `errorCode`, and `HttpStatus`. A single `@ExceptionHandler(AppException.class)` catches every module exception and returns a consistent error shape:

```json
{
  "timestamp": "2026-08-09T10:30:00",
  "message": "This event is fully booked",
  "details": "uri=/api/bookings/3",
  "errorCode": "EVENT_FULL"
}
```

---

## Setup

### Prerequisites

- Java 21
- Maven
- MySQL 8.0

### Database

```sql
CREATE DATABASE event_booking;

USE event_booking;
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
```

### Environment Variables

| Variable | Description |
|----------|-------------|
| `DB_PASSWORD` | MySQL root password |
| `JWT_SECRET` | Base64-encoded secret key (min 32 bytes) |

Generate a JWT secret:
```bash
openssl rand -base64 32
```

### Run Locally

```bash
mvn clean package -DskipTests
java -jar target/event-booking-0.0.1-SNAPSHOT.jar
```

API available at `http://localhost:8080`.

---

## Example Requests

### Register
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Ahmed",
  "username": "ahmed",
  "email": "ahmed@mail.com",
  "password": "password123"
}
```

### Login
```http
POST /api/auth/login
Content-Type: application/json

{
  "usernameOrEmail": "ahmed",
  "password": "password123"
}
```

Response:
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer"
}
```

### Create Event (ADMIN)
```http
POST /api/events
Authorization: Bearer {token}
Content-Type: application/json

{
  "title": "Spring Boot Workshop",
  "description": "Hands-on Spring Boot fundamentals",
  "venue": "Cairo Tech Hub",
  "eventDate": "2026-09-15T10:00:00",
  "capacity": 50
}
```

### Book an Event (USER)
```http
POST /api/bookings/1
Authorization: Bearer {token}
```

---

## Notes

- Roles must be seeded manually before registration — new users are auto-assigned `ROLE_USER`
- `bookedCount` on `Event` avoids counting bookings via a live query on every request
- All passwords are hashed with BCrypt before storage
