# Kashy Dependency Update Plan

**Generated:** March 8, 2026  
**Scope:** Java 25 + Spring Boot 4 Maven multi-module project

---

## Executive Summary

| Category | Count | Action |
|----------|-------|--------|
| **High-confidence (safe)** | 6 | Apply in Phase 1 |
| **Medium-confidence** | 2 | Apply in Phase 2 with verification |
| **Defer (milestone/beta)** | 2 | Skip for now |
| **Transitive (managed by Spring BOM)** | N/A | Resolved by Spring Boot upgrade |

**Vulnerability status:** No known critical CVEs in current versions. Transitive dependencies `commons-text` (1.14.0) and `snakeyaml` (2.5) are at patched levels.

---

## Phase 1: High-Confidence Upgrades (Smallest Safe Update)

Apply these changes first. All are patch/minor updates with low breaking-change risk.

### 1. Spring Boot 4.0.2 → 4.0.3 (parent POM)

**File:** `pom.xml`  
**Change:** Update parent version from `4.0.2` to `4.0.3`

```xml
<parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>4.0.3</version>  <!-- was 4.0.2 -->
  ...
</parent>
```

**Rationale:** Maintenance release (Feb 2026) with 47 bug fixes and dependency upgrades. Same minor line, no API changes expected.

**Breaking changes:** None expected. See [Spring Boot 4.0.3 release notes](https://spring.io/blog/2026/02/19/spring-boot-4-0-3-available-now).

---

### 2. Swagger Annotations 2.2.30 → 2.2.44

**File:** `pom.xml`  
**Property:** `swagger-annotations.version`

```xml
<swagger-annotations.version>2.2.44</swagger-annotations.version>
```

**Rationale:** Patch update for OpenAPI/Swagger annotations. Annotation-only library; low risk.

---

### 3. PostgreSQL JDBC Driver 42.7.9 → 42.7.10

**Managed by:** Spring Boot BOM (transitive). If overridden in `dependencyManagement`, update:

```xml
<postgresql.version>42.7.10</postgresql.version>  <!-- if explicitly set -->
```

**Rationale:** Patch release; driver compatibility maintained.

---

### 4. Spotless Maven Plugin 3.1.0 → 3.3.0

**File:** `pom.xml`  
**Property:** `spotless.version`

```xml
<spotless.version>3.3.0</spotless.version>
```

**Rationale:** Formatting plugin; behavior should remain consistent. Run `mvn spotless:apply` after upgrade to verify.

---

### 5. OpenAPI Generator Maven Plugin 7.19.0 → 7.20.0

**File:** `pom.xml`  
**Property:** `openapi-generator.version`

```xml
<openapi-generator.version>7.20.0</openapi-generator.version>
```

**Rationale:** Minor version bump. Generated code may have small differences; regenerate and diff before committing.

**Verification:** Run `mvn clean generate-sources` and ensure generated API/model code compiles and tests pass.

---

### 6. Maven Surefire & Failsafe 3.5.4 → 3.5.5

**File:** `pom.xml`  
**Properties:** `maven-surefire-plugin.version`, `maven-failsafe-plugin.version`

```xml
<maven-surefire-plugin.version>3.5.5</maven-surefire-plugin.version>
<maven-failsafe-plugin.version>3.5.5</maven-failsafe-plugin.version>
```

**Rationale:** Patch releases; test execution behavior unchanged.

---

## Phase 2: Medium-Confidence (Apply After Phase 1)

### 7. MapStruct 1.6.3 → 1.7.0.Beta1 — **Defer**

**Recommendation:** Do **not** upgrade. `1.7.0.Beta1` is a beta; MapStruct 1.6.3 is the latest stable and is current.

**Action:** Keep `1.6.3`. Revisit when MapStruct 1.7.0 is released as stable.

---

### 8. Lombok 1.18.42

**Status:** Already at latest stable. No action.

---

### 9. Testcontainers 2.0.3

**Status:** At latest in 2.x line. Spring Boot 4.0.3 BOM may update this; no explicit change needed unless you override.

---

## Deferred / Do Not Upgrade

| Dependency | Current | Available | Reason |
|------------|---------|-----------|--------|
| Spring Boot | 4.0.2 | 4.1.0-M2 | Milestone; use 4.0.3 instead |
| MapStruct | 1.6.3 | 1.7.0.Beta1 | Beta; stay on stable |

---

## Transitive Dependencies (No Direct Changes)

These are managed by Spring Boot BOM. Upgrading to Spring Boot 4.0.3 will pull updated transitive versions where applicable.

| Dependency | Current | Notes |
|------------|---------|-------|
| commons-text | 1.14.0 | Patched for CVE-2025-46295 (1.14.0+ is safe) |
| snakeyaml | 2.5 | Patched for CVE-2022-1471 (2.0+ is safe) |
| jackson-databind | 3.0.4 | Jackson 3 (tools.jackson.core) |
| Hibernate | 7.2.1.Final | Managed by Spring Boot |
| Tomcat | 11.0.15 | Managed by Spring Boot |

---

## Likely Breaking Changes (If Any)

1. **OpenAPI Generator 7.19 → 7.20:** Generated code structure may differ slightly. Review `ApiUtil.java` and controller/model classes after regeneration.
2. **Spring Boot 4.0.2 → 4.0.3:** Deprecations in Jackson 2 → Jackson 3 migration path (e.g. `Jackson2EndpointAutoConfiguration`) — only relevant if you use Actuator JMX/Jackson 2 endpoints. Your app uses Jackson 3 (`tools.jackson.core`), so impact should be minimal.

---

## Recommended Execution Order

1. **Backup / branch** before changes.
2. Update **Spring Boot parent** to 4.0.3.
3. Run `mvn clean compile` — fix any compile errors.
4. Update **swagger-annotations**, **spotless**, **openapi-generator**, **surefire**, **failsafe** in `pom.xml`.
5. Run `mvn clean generate-sources` (if OpenAPI plugin runs in generate-sources phase).
6. Run `mvn spotless:apply`.
7. Run `mvn verify` (unit + integration tests).
8. Commit with message: `chore: update dependencies (Spring Boot 4.0.3, swagger 2.2.44, spotless 3.3.0, openapi 7.20.0, surefire/failsafe 3.5.5)`.

---

## Optional: Maven Enforcer for Minimum Maven Version

The versions plugin reported that the project does not define a minimum Maven version. Consider adding:

```xml
<plugin>
  <groupId>org.apache.maven.plugins</groupId>
  <artifactId>maven-enforcer-plugin</artifactId>
  <version>3.6.2</version>
  <executions>
    <execution>
      <id>enforce-maven</id>
      <goals><goal>enforce</goal></goals>
      <configuration>
        <rules>
          <requireMavenVersion>
            <version>[3.9.0,)</version>
          </requireMavenVersion>
        </rules>
      </configuration>
    </execution>
  </executions>
</plugin>
```

---

## Summary of pom.xml Edits (Phase 1)

| Property / Element | Old | New |
|-------------------|-----|-----|
| `spring-boot-starter-parent` version | 4.0.2 | 4.0.3 |
| `swagger-annotations.version` | 2.2.30 | 2.2.44 |
| `spotless.version` | 3.1.0 | 3.3.0 |
| `openapi-generator.version` | 7.19.0 | 7.20.0 |
| `maven-surefire-plugin.version` | 3.5.4 | 3.5.5 |
| `maven-failsafe-plugin.version` | 3.5.4 | 3.5.5 |

No changes to: `mapstruct.version`, `lombok.version`, `lombok-mapstruct-binding.version`, `testcontainers.version`, `rest-assured.version`, `jacoco.version`, `flatten-maven-plugin.version`, `maven-compiler-plugin.version`.
