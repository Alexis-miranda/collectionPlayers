# Football Championship Platform

A backend learning project evolving into a football competition management platform built with Java and Spring Boot microservices.

The objective is not only to expose CRUD endpoints. The project is intended to demonstrate service boundaries, domain modelling, relational persistence with different database engines, inter-service communication, automated testing and reproducible deployments.

> **Status:** In development. This branch updates the build baseline and service-to-service HTTP client approach. Match and championship business capabilities, deployment automation and comprehensive tests remain in progress.
>
> **Accuracy note:** Planned architecture is explicitly separated from existing code. Build-file changes are not considered verified until the full Maven reactor builds and automated tests pass.

## Contents

- [Vision and goals](#vision-and-goals)
- [Target architecture](#target-architecture)
- [Services and current scope](#services-and-current-scope)
- [Technology stack](#technology-stack)
- [Current API surface](#current-api-surface)
- [Data ownership](#data-ownership)
- [Local development](#local-development)
- [Configuration and secrets](#configuration-and-secrets)
- [Engineering standards](#engineering-standards)
- [Modernization roadmap](#modernization-roadmap)

## Vision and goals

The intended platform covers five football-domain capabilities:

1. **Players:** player profiles and basic information.
2. **Teams:** team profiles and squad membership.
3. **Statistics:** player performance data associated with matches and competitions.
4. **Matches:** fixtures, participating teams, dates, match status and results.
5. **Championships:** competitions, seasons, registrations, fixtures and standings.

The project is also a practical environment for demonstrating modern Java, Spring Boot, REST API design, microservice boundaries, persistence with PostgreSQL and MySQL, Maven multi-module builds, automated testing, containerised development, API documentation and observability.

## Target architecture

The target is a set of independently executable services. Each service owns its data and exposes an API contract; no service should read or write another service's database directly.

~~~mermaid
flowchart TB
    Client[API client / future web application]
    Gateway[Optional API Gateway - later phase]

    subgraph Platform[Football Championship Platform]
      Players[Players Service]
      Teams[Teams Service]
      Statistics[Statistics Service]
      Matches[Matches Service]
      Championships[Championships Service]
    end

    PGPlayers[(PostgreSQL - players)]
    PGTeams[(PostgreSQL - teams)]
    MySQLStats[(MySQL - statistics)]
    PGMatches[(PostgreSQL - matches - proposed)]
    PGChampionships[(PostgreSQL - championships - proposed)]

    Client --> Gateway
    Gateway -. routes .-> Players
    Gateway -. routes .-> Teams
    Gateway -. routes .-> Statistics
    Gateway -. routes .-> Matches
    Gateway -. routes .-> Championships

    Teams -->|REST / HTTP interfaces| Players
    Statistics -. uses match and player identifiers .-> Matches
    Championships -. coordinates fixtures .-> Matches

    Players --> PGPlayers
    Teams --> PGTeams
    Statistics --> MySQLStats
    Matches --> PGMatches
    Championships --> PGChampionships
~~~

The gateway and the future database/service relationships shown above are architectural direction, not deployed components. The current code contains player, team and statistics service implementations; matches and championships are still foundations.

### Communication principles

- Use REST for the current synchronous use cases.
- Use Spring HTTP Service Clients ( `@HttpExchange` backed by `RestClient` ) for declarative HTTP clients.
- Keep remote service base URLs configurable through environment variables.
- Define explicit request/response DTOs instead of sharing persistence entities.
- Add connection/response timeouts and deliberate downstream error handling.
- Keep the dependency one-way: Teams orchestrates squad membership and calls the Players API; Players does not call Teams back.
- Consider asynchronous messaging only when a concrete use case requires it. Kafka and RabbitMQ are not currently implemented.

## Services and current scope

| Module | Responsibility | Current state |
|---|---|---|
| `msvc-jugadores` | Player CRUD and player data | Existing REST/JPA implementation; PostgreSQL |
| `msvc-equipos` | Team CRUD and squad workflows | Existing REST/JPA implementation; PostgreSQL |
| `msvc-estadistica` | Player statistics | Initial REST/JPA implementation; MySQL |
| `msvc-partidos` | Fixtures, results and match lifecycle | Application scaffold; domain API and persistence remain to be implemented |
| `msvc-campeonatos` | Competitions, seasons, registrations and standings | Application scaffold; domain API and persistence remain to be implemented |

### Capabilities identified in the current source

**Players**
- List players and retrieve a player by ID.
- Create, update and delete player records.
- Validate request bodies on create/update.

**Teams**
- List teams and retrieve a team with associated players.
- Create, update and delete teams.
- Assign, create and remove players through team workflows.
- Remove a team-player association by its ID.

**Statistics**
- List and retrieve statistics records.
- Create and update statistics records.
- Persist statistics in MySQL.

These are observations from the source, not claims that every workflow has been integration-tested or is production-ready.

## Technology stack

### Modernization baseline configured in this branch

| Technology | Configured target | Purpose |
|---|---|---|
| Java | 25 | Current LTS Java baseline |
| Spring Boot | 4.1.2 | Application framework and dependency management |
| Spring Framework | 7.x (managed by Boot) | Core framework and HTTP service interfaces |
| Maven Wrapper | 3.10.1 | Reproducible builds |
| Spring HTTP Service Clients | `@HttpExchange` + `RestClient` | Declarative synchronous service communication |
| Spring Data JPA | Managed by Spring Boot | Relational persistence |
| PostgreSQL | Retained | Players and teams |
| MySQL | Retained | Player statistics |
| Jakarta Bean Validation | Managed by Spring Boot | Request validation |

The branch aligns build files to this baseline. The migration is **not verified until a clean full-reactor build and tests succeed**.

### Planned supporting technologies

| Technology | Why | Status |
|---|---|---|
| Docker / Docker Compose | Start services and both database engines consistently | Planned |
| Flyway | Version and reproduce schema changes | Planned |
| Testcontainers | Test against real PostgreSQL and MySQL containers | Planned |
| Spring Boot Actuator | Health, readiness and metrics | Planned |
| OpenAPI / Swagger UI | Publish and explore API contracts | Planned |
| GitHub Actions | Build and test on pull requests | Planned |
| Structured logging / Micrometer | Diagnostics and observability | Planned |
| API Gateway | Single entry point if justified by service count | Later evaluation |
| Kafka or RabbitMQ | Event-driven workflows where useful | Not implemented; evaluate later |

The project intentionally keeps **both PostgreSQL and MySQL**. They serve different service contexts and provide practical experience with two relational database engines.

## Current API surface

These route patterns were identified in the existing controllers. They are not a final versioned API contract. Consistent base paths, DTOs, pagination and error formats remain to be designed.

### Players — port 8005

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | List players |
| GET | `/{id}` | Retrieve a player |
| POST | `/` | Create a player |
| PUT | `/{id}` | Update a player |
| DELETE | `/{id}` | Delete a player |

### Teams — port 8006

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | List teams |
| GET | `/{id}` | Retrieve a team |
| POST | `/` | Create a team |
| PUT | `/{id}` | Update a team |
| DELETE | `/{id}` | Delete a team |
| PUT | `/asignar-jugador/{equipoId}` | Assign a player |
| POST | `/crear-jugador/{equipoId}` | Create a player through the team workflow |
| DELETE | `/eliminar-jugador/{equipoId}` | Remove a player from a team |
| DELETE | `/eliminar-equipo-jugador/{id}` | Remove a team-player association |

### Statistics — port 8007

| Method | Route | Purpose |
|---|---|---|
| GET | `/` | List statistics |
| GET | `/{id}` | Retrieve a statistics record |
| POST | `/` | Create a statistics record |
| PUT | `/{id}` | Update a statistics record |

### Planned APIs

Matches and Championships APIs will be designed before implementation. Their initial contracts should cover match scheduling/rescheduling/cancellation and result registration; championship and season management; team registration; fixture generation; standings calculation; and validation of team eligibility and match/competition relationships.

No routes for these planned capabilities are advertised as available yet.

## Data ownership

| Service | Database engine | Data owned |
|---|---|---|
| Players | PostgreSQL | Player profiles |
| Teams | PostgreSQL | Team profiles and squad membership |
| Statistics | MySQL | Player performance/statistics |
| Matches | PostgreSQL (proposed) | Fixtures, match status and results |
| Championships | PostgreSQL (proposed) | Competitions, seasons, registrations and standings |

A service owns its schema and is the only service allowed to modify its data. Other services use API contracts and stable identifiers rather than database-level foreign keys across service boundaries.

The database allocation for Matches and Championships will be confirmed when those services are implemented.

## Local development

### Prerequisites

- JDK 25.
- Git.
- Docker Desktop or Docker Engine for the planned Compose environment.
- The included Maven Wrapper.

### Build and test

From the repository root:

~~~bash
./mvnw clean verify
~~~

On Windows:

~~~powershell
.\mvnw.cmd clean verify
~~~

Run an individual service after the reactor has been built:

~~~bash
./mvnw -pl msvc-jugadores spring-boot:run
./mvnw -pl msvc-equipos spring-boot:run
./mvnw -pl msvc-estadistica spring-boot:run
~~~

These commands describe the intended Maven workflow after the reactor migration. They must be confirmed by a clean build and runtime smoke tests before being treated as verified. The database services must also be running and configured.

## Configuration and secrets

The database connection settings currently in the repository still reflect an earlier local-development setup. They do not describe the new database instance and must be replaced when database configuration is revisited.

This modernization has made the Players ↔ Teams HTTP base URLs configurable:

- `EQUIPOS_SERVICE_URL` — base URL used by Players.
- `JUGADORES_SERVICE_URL` — base URL used by Teams.

Database credential externalization is deliberately scheduled for the Docker Compose/configuration phase. At that point, JDBC URLs, usernames and passwords should be injected through environment variables or an ignored local `.env` file, with a committed `.env.example` containing placeholders only. Production secrets should be supplied by the deployment platform or a secrets manager.

Do not add credentials for the new database to source control or to this README.

## Engineering standards

The target quality bar is:

- Clear service boundaries and data ownership.
- DTOs at API boundaries; persistence entities remain internal.
- Consistent validation and global exception handling.
- Correct HTTP status codes and predictable error payloads.
- Explicit client timeouts and downstream failure handling.
- Flyway-managed schema migrations.
- Unit tests for domain/service logic and web-layer behaviour.
- Integration tests using Testcontainers with PostgreSQL and MySQL.
- API contract documentation and repeatable smoke tests.
- Actuator health/readiness endpoints and useful structured logs.
- Automated build, test and dependency/security checks in CI.

A successful compilation alone is not sufficient evidence of correctness.

## Modernization roadmap

The work is split into verifiable increments. Items are only marked complete when implementation has been checked.

### Phase 1 — Build foundation

- [x] Establish one root Maven parent for all services.
- [x] Add Championships to the root Maven reactor.
- [x] Correct Matches module packaging so it can become an executable service.
- [x] Align build files to Spring Boot 4.1.2 / Java 25.
- [x] Update Maven Wrapper distribution to 3.10.1.
- [x] Replace OpenFeign declarations with Spring HTTP Service Client interfaces.
- [x] Make the Teams → Players service base URL configurable.
- [ ] Run a clean full-reactor build and resolve migration issues.
- [ ] Start each service and verify runtime/database connectivity.

### Phase 2 — Service and API design

- [ ] Define API base paths and versioning.
- [ ] Introduce request/response DTOs; stop exposing persistence entities.
- [ ] Standardise validation, exception handling and error responses.
- [x] Remove the unused reverse Players → Teams HTTP client; Teams remains the owner of squad membership workflows.
- [ ] Define the Matches domain model and API.
- [ ] Define the Championships domain model and API.
- [ ] Add pagination, sorting and filtering where needed.

### Phase 3 — Persistence and local environment

- [ ] Externalise database URLs and credentials.
- [ ] Add Dockerfiles and Docker Compose for services, PostgreSQL and MySQL.
- [ ] Add health checks and startup dependency handling.
- [ ] Add Flyway migrations for service-owned schemas.
- [ ] Document fresh database initialization and local reset procedures.

### Phase 4 — Testing and reliability

- [ ] Add meaningful unit tests for every service.
- [ ] Add web-layer and repository integration tests.
- [ ] Add Testcontainers coverage for PostgreSQL and MySQL.
- [ ] Test service-to-service calls and downstream failures.
- [ ] Configure explicit connection/read timeouts and resilience behaviour.
- [ ] Add OpenAPI documentation and API smoke tests.

### Phase 5 — Delivery and observability

- [ ] Add GitHub Actions for build and test.
- [ ] Add dependency and security scanning.
- [ ] Add Actuator health/readiness endpoints.
- [ ] Add structured logs and metrics.
- [ ] Document deployment profiles and operational configuration.
- [ ] Evaluate whether an API Gateway or asynchronous messaging is justified.

## Repository layout

~~~text
.
├── msvc-jugadores/       # Player service (PostgreSQL)
├── msvc-equipos/         # Team service (PostgreSQL)
├── msvc-estadistica/     # Player statistics service (MySQL)
├── msvc-partidos/        # Match service foundation
├── msvc-campeonatos/     # Championship service foundation
├── .mvn/                 # Maven Wrapper configuration
├── mvnw                  # Maven Wrapper (Unix)
├── mvnw.cmd              # Maven Wrapper (Windows)
└── pom.xml               # Root parent and Maven reactor
~~~

## Project status

This is an evolving portfolio project. The objective is a well-structured, testable and reproducible microservices system—not adding technologies for their own sake. Roadmap items will only be marked complete after implementation and verification.
