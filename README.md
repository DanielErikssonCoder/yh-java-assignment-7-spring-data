# Member API - Spring Data REST Service

A member management REST API, demonstrating Spring Data JPA, layered architecture, and role-based security, built with Spring Boot and H2.

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5-6DB33F.svg)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6-6DB33F.svg)](https://spring.io/projects/spring-security)
[![H2](https://img.shields.io/badge/Database-H2_In--Memory-1E90FF.svg)](https://www.h2database.com/)

## Table of Contents

- [About the Project](#about-the-project)
- [Features](#features)
- [Project Structure](#project-structure)
- [Architecture & Design](#architecture--design)
- [How to Run](#how-to-run)
- [Usage](#usage)
- [Technical Implementation](#technical-implementation)

## About the Project

Built as an individual assignment in 'Java Backend' for the YH education in Java System Development, this project implements a RESTful member management API.

The API supports two roles — **Admin** and **Member** — each with their own set of endpoints and access restrictions. It demonstrates proper use of **Spring Data JPA**, **Spring Security with Basic Auth**, **DTO-based request handling**, and **global exception handling** with correct HTTP status codes throughout.

## Features

### Core Functionality

- **Full CRUD for Admins** - Create, read, update (PUT & PATCH), and delete members via secured endpoints.
- **Member Self-Service** - Members can view the member list and update their own data via `/mypages` endpoints.
- **Role-Based Access Control** - Admin endpoints are locked to `ADMIN` role, member endpoints to `MEMBER` role.
- **DTO Validation** - Incoming requests are validated with Jakarta Bean Validation (`@NotBlank`, `@Email`, `@Pattern`).
- **Global Exception Handling** - Centralized `@RestControllerAdvice` returns correct status codes (404, 409, 403, 400) with descriptive messages.
- **Data Seeding** - Five members and three addresses are seeded into the H2 database on startup.
- **Dynamic Credentials** - Member credentials are auto-generated on creation and printed to the console for easy Postman testing.

### Security

- **HTTP Basic Authentication** throughout.
- Admin credentials: `admin / admin` (configured in `SecurityConfig`).
- Member credentials: auto-generated UUID-based passwords via `CredentialService`, stored in `InMemoryUserDetailsManager`.
- Members are blocked from updating other members' data (ownership check in the controller).

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com.danielerikssoncoder.spring_data_assignment/
│   │       ├── SpringDataAssignmentApplication.java    # Entry point
│   │       │
│   │       ├── config/
│   │       │   ├── DataSeeder.java                     # Seeds 5 members & 3 addresses on startup
│   │       │   └── SecurityConfig.java                 # Basic auth, role rules, H2 console access
│   │       │
│   │       ├── controller/
│   │       │   ├── AdminMemberController.java          # /admin/members — full CRUD
│   │       │   └── MyPagesMemberController.java        # /mypages/members — member self-service
│   │       │
│   │       ├── dto/
│   │       │   ├── MemberDto.java                      # Response DTO for member list (mypages)
│   │       │   ├── MemberRequestDto.java               # Request DTO for admin create/update
│   │       │   └── MemberUpdateDto.java                # Request DTO for mypages PUT
│   │       │
│   │       ├── entity/
│   │       │   ├── Member.java                         # Member entity with JPA mapping
│   │       │   └── Address.java                        # Separate address entity (Many-to-One)
│   │       │
│   │       ├── exception/
│   │       │   ├── GlobalExceptionHandler.java         # Centralized error handling
│   │       │   ├── MemberNotFoundException.java
│   │       │   ├── MemberAlreadyExistsException.java
│   │       │   └── AddressNotFoundException.java
│   │       │
│   │       ├── repository/
│   │       │   ├── MemberRepository.java
│   │       │   └── AddressRepository.java
│   │       │
│   │       └── service/
│   │           ├── MemberService.java                  # Business logic, address resolution
│   │           └── CredentialService.java              # Auto-generates & registers member credentials
│   │
│   └── resources/
│       └── application.properties                      # H2 config, JPA settings
│
└── test/
    └── java/
        └── com.danielerikssoncoder.spring_data_assignment/
            └── SpringDataAssignmentApplicationTests.java
```

## Architecture & Design

### Multi-Layered Architecture

**1. Controller Layer**
- Handles HTTP routing and request/response mapping.
- Delegates all business logic to the service layer.
- Uses DTOs for inbound data — raw entities are never accepted directly from clients.

**2. Service Layer**
- Validates business rules (duplicate email check, address existence).
- Resolves address references from the database before persisting members.
- Generates and registers member credentials on creation.

**3. Repository Layer**
- Extends `JpaRepository` for standard CRUD.
- Custom `findByEmail` query in `MemberRepository`.

**4. Persistence Layer**
- H2 in-memory database, auto-created and seeded on startup.
- `Address` and `Member` in separate tables with a `@ManyToOne` / `@JoinColumn` relationship.

### Design Decisions

- **DTO separation** — `MemberRequestDto` for admin writes, `MemberUpdateDto` for member self-service, `MemberDto` for the restricted member list response.
- **Ownership check** — `MyPagesMemberController` reads the authenticated user's email from `SecurityContextHolder` and compares it to the target member before allowing updates.
- **Dynamic credentials** — Avoids hardcoded member passwords. Each new member gets a generated password printed to the console, making Postman testing straightforward without a persistent credential store.

## How to Run

### Prerequisites

- **Java 21**
- **Maven 3.8+** (or use the included `mvnw` wrapper)

### Build & Run

```bash
# Clone the repository
git clone https://github.com/DanielErikssonCoder/yh-java-assignment-7-spring-data.git

# Navigate to project directory
cd yh-java-assignment-7-spring-data

# Run with Maven wrapper (Windows)
.\mvnw.cmd spring-boot:run

# Or on Unix/Linux/Mac
./mvnw spring-boot:run
```

The application starts on `http://localhost:8080`. The H2 console is available at `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:testdb`, username: `sa`, no password).

On startup, member credentials are printed to the console:

```
Credentials:
anna@example.com | a1b2c3d4
bengt@example.com | e5f6g7h8
...
```

Use these in Postman with Basic Auth to test the `/mypages` endpoints.

## Usage

### Admin Endpoints

All requests use Basic Auth with `admin / admin`.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/admin/members` | List all members (full data) |
| GET | `/admin/members/{id}` | Get a specific member |
| POST | `/admin/members` | Create a new member |
| PUT | `/admin/members/{id}` | Replace all member data |
| PATCH | `/admin/members/{id}` | Update specific fields |
| DELETE | `/admin/members/{id}` | Delete a member |

### Member Endpoints

All requests use Basic Auth with the member's email and generated password.

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/mypages/members` | List all members (limited fields) |
| PUT | `/mypages/members/{id}` | Update own data (other members return 403) |

### Example POST Body

```json
{
  "firstName": "Erik",
  "lastName": "Svensson",
  "email": "erik@example.com",
  "phone": "0701234567",
  "dateOfBirth": "1990-03-15",
  "address": { "id": 1 }
}
```

### Example PATCH Body

```json
{
  "firstName": "Uppdaterat namn",
  "phone": "0709876543"
}
```

## Technical Implementation

### Spring Data JPA

- Entities use `@Column` with explicit `length`, `nullable`, and `unique` constraints.
- `dateOfBirth` is unique across members as required by the spec.
- `phone` is nullable — the only field that allows null.
- `Address` is a separate entity in its own table, shared across members via `@ManyToOne`.

### Spring Security

- `SecurityConfig` defines a `SecurityFilterChain` with role-based `requestMatchers`.
- `InMemoryUserDetailsManager` holds both the admin account and all dynamically created member accounts.
- CSRF is disabled for stateless REST API usage.
- H2 console access is explicitly permitted and frame options disabled.

### Exception Handling

`GlobalExceptionHandler` maps custom exceptions to HTTP responses:

| Exception | Status |
|-----------|--------|
| `MemberNotFoundException` | 404 Not Found |
| `AddressNotFoundException` | 404 Not Found |
| `MemberAlreadyExistsException` | 409 Conflict |
| `MethodArgumentNotValidException` | 400 Bad Request |

---

**Author:** Daniel Eriksson  
**Course:** Java System Development (YH)  
**Assignment:** Spring Data & REST API   
**Date:** March 2026   
