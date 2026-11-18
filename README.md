# Football Championship Microservices

A Java/Spring Boot learning project that models a football domain through independently organised REST services. The repository is being developed iteratively: player, team and player-statistics capabilities are present in the source tree, while match and championship modules are still scaffolding / work in progress.

> **Project status:** In development · Not production-ready  
> **Documentation status:** This README describes the repository as it currently exists. Planned capabilities are explicitly separated from implemented code.

## Contents

- [Project goals](#project-goals)
- [Architecture](#architecture)
- [Services and current scope](#services-and-current-scope)
- [Technology baseline](#technology-baseline)
- [API surface](#api-surface)
- [Data stores](#data-stores)
- [Local development](#local-development)
- [Quality and security](#quality-and-security)
- [Roadmap](#roadmap)
- [Repository structure](#repository-structure)

## Project goals

The project explores how to split a football management domain into backend services, each responsible for a focused capability. It is intended to practise:

- REST API design and resource-oriented CRUD operations.
- Service boundaries and synchronous service-to-service communication.
- Persistence with Spring Data JPA and relational databases.
- Input validation and HTTP response handling.
- Multi-module Maven builds and container-oriented development.

## Architecture

The current source contains three service areas with application code and two additional module skeletons.

```mermaid
flowchart LR
    Client[API client / Postman]
    Teams[Teams service\n:8006]
    Players[Players service\n:8005]
    Stats[Player statistics service\n:8007]
    PG1[(PostgreSQL\nplayers)]
    PG2[(PostgreSQL\nteams)]
    MySQL[(MySQL\nstatistics)]
    Matches[Matches module\nscaffold]
    Championships[Championships module\nscaffold]

    Client --> Players
    Client --> Teams
    Client --> Stats
    Teams <-->|Synchronous REST\nOpenFeign (current)| Players
    Players --> PG1
    Teams --> PG2
    Stats --> MySQL
    Matches -. planned .-> Teams
    Championships -. planned .-> Matches
```

**Important architecture note:** the current code uses synchronous HTTP calls between players and teams. This is useful for learning, but it creates runtime coupling and should be reviewed before production use. The diagram describes the current code layout, not a fully deployed environment.

## Services and current scope

| Module | Current status | Responsibility / evidence in source |
|---|---|---|
| `msvc-jugadores` | Implemented, needs hardening | Player REST controller, CRUD service/repository and PostgreSQL configuration. |
| `msvc-equipos` | Implemented, needs hardening | Team REST controller, team-player association and calls to the players service. PostgreSQL configuration. |
| `msvc-estadistica` | Initial implementation | Player-statistics REST controller, JPA persistence and MySQL configuration. |
| `msvc-partidos` | Scaffold / work in progress | Spring Boot application class exists; no completed match API or persistence flow is documented in the current source. |
| `msvc-campeonatos` | Scaffold / work in progress | Separate module directory and application class exist; it is not currently included in the root Maven reactor. |

### Implemented capabilities observed in the code

**Players**
- List players and retrieve a player by ID.
- Create, update and delete player records.
- Validate request bodies on create/update.

**Teams**
- List teams and retrieve a team with its associated players.
- Create, update and delete teams.
- Assign, create and remove players through the players service.
- Remove a team-player association by its ID.

**Player statistics**
- List and retrieve player-statistics records.
- Create and update statistics records.
- Persist statistics using MySQL.

These are source-level observations, not a claim that every endpoint has been integration-tested. Error handling, validation consistency and response contracts still need work.

## Technology baseline

The versions below are the **current repository baseline**, not the proposed modernization target.

| Technology | Current repository configuration |
|---|---|
| Java | 17 |
| Spring Boot | 3.4.4 in the root parent; the statistics and matches modules declare their own 3.4.4 parent |
| Spring Cloud | 2024.0.1 in modules that use OpenFeign |
| Build | Maven Wrapper configured for Maven 3.9.9 |
| Persistence | Spring Data JPA / Hibernate |
| Databases | PostgreSQL for players and teams; MySQL for statistics |
| Inter-service HTTP | Spring Cloud OpenFeign |
| Validation | Jakarta Bean Validation / Spring Boot validation starter |
| Tests | Spring Boot Test starter is declared; meaningful automated coverage still needs to be expanded |
| API client | Postman (manual testing) |

## API surface

The controllers currently expose the following route patterns. Exact base URLs and request/response schemas should be confirmed as the API contract is standardised.

### Players — port 8005

| Method | Route pattern | Purpose |
|---|---|---|
| GET | `/` | List players |
| GET | `/{id}` | Get player by ID |
| POST | `/` | Create a player |
| PUT | `/{id}` | Update a player |
| DELETE | `/{id}` | Delete a player |

### Teams — port 8006

| Method | Route pattern | Purpose |
|---|---|---|
| GET | `/` | List teams |
| GET | `/{id}` | Get team details |
| POST | `/` | Create a team |
| PUT | `/{id}` | Update a team |
| DELETE | `/{id}` | Delete a team |
| PUT | `/asignar-jugador/{equipoId}` | Assign a player to a team |
| POST | `/crear-jugador/{equipoId}` | Create a player through the team workflow |
| DELETE | `/eliminar-jugador/{equipoId}` | Remove a player from a team |
| DELETE | `/eliminar-equipo-jugador/{id}` | Delete a team-player association |

### Player statistics — port 8007

| Method | Route pattern | Purpose |
|---|---|---|
| GET | `/` | List statistics |
| GET | `/{id}` | Get statistics by ID |
| POST | `/` | Create statistics |
| PUT | `/{id}` | Update statistics |

> The controllers do not currently declare explicit `@RequestMapping` base paths. Route prefixes, API versioning, pagination and consistent error payloads are part of the API-hardening roadmap.

## Data stores

The project intentionally uses two relational database technologies to practise different persistence configurations.

- **PostgreSQL:** players and teams.
- **MySQL:** player statistics.

Each service should own its persistence model. Cross-service relationships should use identifiers and service contracts rather than database-level foreign keys between services.

Database URLs, usernames and passwords must be provided through environment variables or an external secrets manager. Never commit real credentials to Git. If credentials have ever been pushed to a public repository, rotate them; deleting them from the latest commit is not sufficient.

## Local development

### Prerequisites

- JDK 17 (current baseline).
- Git.
- Docker / Docker Compose for local database services (container orchestration is being standardised).
- The included Maven Wrapper.

### Build status

The Maven project structure is currently inconsistent: some child modules inherit from the root aggregator while others declare Spring Boot directly as their parent; the matches module is packaged as a POM; and the championships module is not included in the root reactor.

For that reason, a single root-level build command is **not yet documented as a verified working workflow**. Normalising the Maven reactor and adding a reproducible Compose-based environment are explicit modernization tasks. Until then, use the module POMs as the source of truth and do not assume every module can be started together.

## Quality and security

Before describing this repository as production-ready, the project needs:

- Automated unit and integration tests for each service.
- Testcontainers-based database integration tests.
- Consistent DTOs, validation and global exception handling.
- Externalised configuration and secret rotation.
- Health/readiness endpoints and structured application logs.
- Timeouts and resilient handling for inter-service calls.
- Database migrations (for example, Flyway) instead of relying on automatic schema generation.
- OpenAPI documentation and a repeatable local environment.

## Roadmap

### Foundation
- [ ] Align the Maven parent/child hierarchy and module list.
- [ ] Upgrade to a supported Java and Spring Boot baseline.
- [ ] Replace embedded connection details with environment-based configuration.
- [ ] Add Docker Compose for PostgreSQL and MySQL.

### API and service design
- [ ] Define versioned API contracts and DTOs.
- [ ] Standardise validation and error responses.
- [ ] Review bidirectional synchronous coupling between players and teams.
- [ ] Evaluate Spring HTTP Service Clients as the modern alternative to OpenFeign.
- [ ] Add resilience policies and explicit connection/read timeouts.

### Reliability and delivery
- [ ] Add unit, integration and contract tests.
- [ ] Add Flyway migrations.
- [ ] Add OpenAPI / Swagger UI.
- [ ] Add CI checks for build, tests and dependency vulnerabilities.
- [ ] Complete matches and championships as separate, verifiable capabilities.

## Repository structure

```text
.
├── msvc-jugadores/       # Player service
├── msvc-equipos/         # Team service
├── msvc-estadistica/     # Player statistics service
├── msvc-partidos/        # Matches module (work in progress)
├── msvc-campeonatos/     # Championships module (not in root reactor yet)
├── pom.xml               # Root Maven aggregator
├── mvnw                  # Maven Wrapper (Unix)
└── mvnw.cmd              # Maven Wrapper (Windows)
```

## Project status

This is an actively evolving portfolio / learning project. Features, deployment instructions and technology versions will be updated as they are implemented and verified. The README deliberately distinguishes current code from planned work.
