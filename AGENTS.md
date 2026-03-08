# Kashy

Personal finance management app (Java 25, Spring Boot 4, PostgreSQL). See `README.md` and `kashy-api/README.md` for full docs.

## Cursor Cloud specific instructions

### Prerequisites (pre-installed in VM snapshot)

- **JDK 25 (Temurin)** at `/usr/lib/jvm/jdk-25` — `JAVA_HOME` is set in `~/.bashrc`
- **Maven 3.9+** at `/opt/maven`
- **Docker** — needed for Testcontainers (integration tests) and for the PostgreSQL dev database

### Starting services

1. **Docker daemon** must be started before running integration tests or the app:
   ```bash
   sudo dockerd &>/tmp/dockerd.log &
   sleep 3
   sudo chmod 666 /var/run/docker.sock
   ```

2. **PostgreSQL 16** (for running the app, not needed for unit tests):
   ```bash
   docker run -d --name kashy-postgres \
     -e POSTGRES_DB=kashy -e POSTGRES_USER=kashy -e POSTGRES_PASSWORD=kashy \
     -p 5432:5432 postgres:16-alpine
   ```
   If the container already exists: `docker start kashy-postgres`

3. **Run the application** (from `kashy-api/`):
   ```bash
   cd kashy-api && mvn spring-boot:run
   ```
   API available at `http://localhost:8080/api/v1/expenses`

### Key commands

| Task | Command |
|------|---------|
| Full build + all tests | `mvn clean install` |
| Compile only (generates OpenAPI code) | `mvn clean compile` |
| Unit tests only | `mvn test` |
| Integration tests only (needs Docker) | `mvn verify -DskipUnitTests=true` |
| Lint check | `mvn spotless:check` |
| Lint fix | `mvn spotless:apply` |

### Gotchas

- **No Maven wrapper (`mvnw`)** is checked in; Maven must be on `PATH` (installed at `/opt/maven/bin/mvn`).
- **Integration tests (`*IT.java`) require Docker** — Testcontainers starts its own `postgres:16-alpine` containers, so the manually started PostgreSQL container is *not* needed for tests, only for running the app.
- **OpenAPI code generation** runs during `generate-sources` phase automatically on compile. Generated code lands in `target/generated-sources/openapi/`.
- `JAVA_HOME` must point to JDK 25; the build uses `<java.version>25</java.version>`.
