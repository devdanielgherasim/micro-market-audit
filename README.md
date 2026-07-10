# audit

Audit log microservice for the microservices dissertation project. It runs on port **8089** and is a **leaf service**: it has no `clients/` package and makes no outbound REST calls to any other microservice. `catalog` and `orders` write to it in a fire-and-forget manner (the caller is responsible for not letting a failed/slow audit write block or fail its own primary operation — that resilience logic lives in the callers, not here). This service's only job is to persist and expose audit log entries: who did what, to which entity, when, from where, and with what result.

## Tech stack

Confirmed from `pom.xml` (Quarkus 3.18.3, Java 21):

- `quarkus-rest` — JAX-RS resources (imperative, not reactive)
- `quarkus-hibernate-orm`, `quarkus-hibernate-orm-panache` — Panache active-record persistence
- `quarkus-hibernate-orm-rest-data-panache` (present, though the controller in this repo is hand-written rather than generated)
- `quarkus-jdbc-postgresql` — PostgreSQL driver
- `quarkus-hibernate-validator` — bean validation (`@NotNull`/`@NotBlank` on DTOs/entity)
- `quarkus-smallrye-openapi` — OpenAPI + Swagger UI
- `quarkus-smallrye-health` — health endpoints
- `quarkus-opentelemetry` — tracing (OTLP exporter, configurable sampler)
- `quarkus-oidc` **and** `quarkus-keycloak-authorization` — both present (unlike `orders`, which is documented as missing `quarkus-keycloak-authorization`)
- `quarkus-arc`, `quarkus-jackson`, `jackson-datatype-jsr310`, `slf4j-api`
- `quarkus-container-image-docker` — used by the native build to produce/push the container image
- Test scope: `quarkus-junit5`, `quarkus-test-security`, `rest-assured`, `testcontainers` (`postgresql` module)

## Local development

```shell
./mvnw quarkus:dev
```

Starts dev mode with hot reload; Dev UI at `http://localhost:8089/q/dev/`.

The service expects PostgreSQL at `localhost:5433` by default (`application.properties`):

```
quarkus.datasource.jdbc.url=jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5433}/${DB_NAME:microservices1691716}
quarkus.datasource.username=${DB_USERNAME:postgres}
quarkus.datasource.password=${DB_PASSWORD:oRncHiOovwJAVOXK}
```

Override any of `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USERNAME`, `DB_PASSWORD` via environment variables to point at a different database.

`quarkus.hibernate-orm.database.generation` defaults to `drop-and-create` (overridable via `HIBERNATE_GENERATION`), so **every dev-mode restart drops and recreates the schema**. There is no `import.sql` in `src/main/resources`, so the schema starts empty on each restart — no seed data is loaded automatically.

Other locally relevant defaults: HTTP port via `HTTP_PORT` (default `8089`), CORS enabled for `localhost:3000`/`localhost:8080` and the Azure hostname, JSON console logging, OpenTelemetry OTLP export enabled by default and pointed at `localhost:4317`.

## API docs

- Swagger UI: `/swagger-ui`
- OpenAPI document: `/openapi`
- Liveness: `/health/live`
- Readiness: `/health/ready`

## Endpoints

All exposed under `/api/audit` by `AuditLogController`:

- `GET /api/audit` — paginated list with optional filters (`startDate`, `endDate`, `username`, `action`, `entityType`, `entityId`)
- `GET /api/audit/{id}` — fetch one entry
- `POST /api/audit` — create an entry (auto-fills `username` from the security identity if not supplied)
- `PUT /api/audit/{id}` — update an entry
- `DELETE /api/audit/{id}` — delete an entry
- `GET /api/audit/action/{action}`, `/entity-type/{entityType}`, `/entity-id/{entityId}`, `/user/{user}`, `/time-range`, `/ip-address/{ipAddress}`, `/status-code/{statusCode}` — paginated filtered lookups

The `AuditLog` entity (`entities/AuditLog.java`) fields: `id` (from `PanacheEntity`), `timestamp`, `action`, `entityType`, `entityId`, `username`, `details` (4000 chars), `ipAddress`, `userAgent`, `statusCode`.

Auth note: `application.properties` marks `/api/audit` itself as `permit` for `GET`/`POST` (so top-level list/create work without a token — matching how fire-and-forget writes from catalog/orders reach this service), while every other path (including `/api/audit/{id}`, updates, deletes) falls under the default `authenticated` policy. This is verified by the test suite (`rejectsAnonymousReadById` expects 401 on `GET /api/audit/{id}` without auth, while creating and listing at `/api/audit` work anonymously).

## Testing

```shell
./mvnw test
```

Real test suite under `src/test/java/cloud/microservices/audit/`:

- `PostgresTestResource.java` — a `QuarkusTestResourceLifecycleManager` that spins up a `postgres:16-alpine` Testcontainer and points the datasource at it (schema `drop-and-create`, no load script, OpenTelemetry disabled for tests)
- `repositories/AuditLogRepositoryTest.java` — `@QuarkusTest` + `@TestTransaction`, persists an `AuditLog` and verifies the repository's count-by-action/entity-type/entity-id/user/status-code queries
- `controllers/AuditLogControllerTest.java` — `@QuarkusTest`, REST Assured tests covering: anonymous create+list round-trip, anonymous read-by-id rejected (401), authenticated (`@TestSecurity`) read/update/delete lifecycle, and invalid-payload rejection (400)
- `controllers/AuditLogControllerIT.java` — `@QuarkusIntegrationTest` smoke test that hits `/health/ready` against the packaged application

Testcontainers requires a working Docker daemon on the machine running the tests.

## Build

```shell
./mvnw package                                                          # JVM build -> target/quarkus-app/quarkus-run.jar
./mvnw clean package -Dnative -Dquarkus.native.container-build=true     # native build via container (what CI runs)
```

## CI/CD

CI runs on GitHub Actions (`.github/workflows/ci.yml`; migrated from GitLab
CI, see `Sources/plans/2026-07-08-gitlab-to-github-migration.md`). Jobs:

- **`test`**: runs `./mvnw test` on a GitHub-hosted runner, publishes JUnit results as a workflow artifact.
- **`build-and-push-native`**: logs into the cloud container registry via the shared `cloud-registry-login` composite action (OIDC), runs `./build.sh` to produce and push the native image, then resolves the pushed image reference/digest via the shared `resolve-image-ref` composite action.
- **`security-scan-gate`** and **`image-supply-chain`**: call the reusable workflows in `devdanielgherasim/micro-market-utilities` — CodeQL (HIGH/CRITICAL severity gate), gitleaks, dependency-review; then Trivy image scan, Syft SBOM generation, cosign keyless sign+verify, and a `repository_dispatch` trigger into the separate `deployment` repo's promotion workflow.

`build.sh` is cloud-provider-aware (`CLOUD_PROVIDER` = `aws`/`azure`/`gcp`, default `aws`), resolves/logs into the matching registry (ECR/ACR/Artifact Registry) unless `CONTAINER_REGISTRY_NAME` is already exported by a caller, then runs `mvn ... clean package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.push=true ...`. Depending on cloud/auth mode it needs some combination of `CONTAINER_REGISTRY_NAME`, `AWS_ACCOUNT_ID`/`AWS_REGION` (or `AWS_ROLE_ARN` + OIDC), `ARM_CLIENT_ID`/`ARM_CLIENT_SECRET` (or Azure OIDC), `GCP_REGION`/`GCP_PROJECT_ID`, plus `CI_COMMIT_SHA`, `CI_PROJECT_NAME`, `PROJECT_NAMESPACE`, `ENVIRONMENT` — not meant to be run ad hoc without those set.

## Auth

Uses `quarkus-oidc` against the shared `microservices` Keycloak realm (`quarkus.oidc.auth-server-url` / `quarkus.oidc.client-id=${KEYCLOAK_CLIENT_ID:audit-service}` / `quarkus.oidc.credentials.secret`), plus `quarkus-keycloak-authorization` (confirmed present in `pom.xml`, unlike `orders`). Default policy is `authenticated` for all paths except `/health/*` (public) and `GET`/`POST` on `/api/audit` (public, see the Endpoints section above).

## Known inconsistencies

- `application.properties` ships a fallback DB password default, `${DB_PASSWORD:oRncHiOovwJAVOXK}`, matching the pattern used across all three Java services. This is an accepted lab-scope default (see the workspace-level `microservices/CLAUDE.md` known-inconsistency list) — not something this README is asking to be rotated.
