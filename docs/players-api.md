# Players Service API

Base path: `/api/v1/players`  
Default port: `8005`

The service owns player profile data. It does not own club membership, competition registration, match events or player statistics. Those belong to their respective services.

## Data contract

Create/update request fields:
- `nombre` (required, 1–80 chars)
- `apellidos` (optional, max 80)
- `fechaNacimiento` (optional ISO date; must be in the past)
- `nacionalidad` (optional, max 60)
- `posicion` (optional: GOALKEEPER, DEFENDER, MIDFIELDER, FORWARD)
- `piePreferido` (optional: RIGHT, LEFT, BOTH)
- `alturaCm` (optional, 100–230)
- `dorsal` (optional, 1–99; compatibility field during the Teams membership refactor)
- `activo` (optional, defaults to true)

The API returns a dedicated response DTO. JPA entities are never serialized by controllers.

## Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/v1/players?page=0&size=20` | Paginated player list |
| GET | `/api/v1/players/{id}` | Player detail |
| GET | `/api/v1/players/by-ids?ids=1&ids=2` | Bulk lookup for other services |
| POST | `/api/v1/players` | Create player |
| PUT | `/api/v1/players/{id}` | Replace editable player fields |
| DELETE | `/api/v1/players/{id}` | Delete player |

HTTP status conventions: 201 create, 200 read/update, 204 delete, 400 validation, 404 missing resource, 500 unexpected server error.

## Compatibility

The existing Teams client currently calls the legacy root routes and `/jugadores-por-equipo`. Those routes remain temporarily available and return the same DTO shape (`id`, `nombre`, `dorsal`) plus the additional profile fields. Teams can migrate to the versioned API in its own implementation step.

## Local configuration

Required runtime configuration is externalised:
- `PLAYERS_DB_URL`
- `PLAYERS_DB_USERNAME`
- `PLAYERS_DB_PASSWORD`
- `SERVER_PORT` (optional; defaults to 8005)

The default JDBC URL targets local PostgreSQL database `msvcJugadores`. Schema update is enabled for this development increment; a Flyway migration and validate-only production profile are planned before production use.

Health endpoint: `/actuator/health`.

## Verification

Run from repository root:
```bash
./mvnw -pl msvc-jugadores -am clean verify
```

The integration test uses isolated H2 and exercises create, list, detail, update, delete, validation errors, 404 and the legacy bulk-lookup contract. It does not prove connectivity to a developer's PostgreSQL instance; that is a separate runtime smoke test.
