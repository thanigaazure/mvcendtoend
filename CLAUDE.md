# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project overview

Spring Boot 3.5.7 (Java 21) server-rendered MVC CRUD demo — a "User Management System" using Thymeleaf templates, Spring Data JPA, and PostgreSQL. Package root: `com.sbmvcprojects.mvcendtoend`.

## Commands

Build, test, and run via the Maven wrapper (no need for a local Maven install):

```
./mvnw compile                 # compile
./mvnw test                    # run all tests
./mvnw test -Dtest=ClassName   # run a single test class
./mvnw spring-boot:run         # run the app locally (needs Postgres reachable per application.properties)
./mvnw clean package           # build the jar (target/mvcendtoend-0.0.1-SNAPSHOT.jar)
```

Docker (builds the jar into an image and runs it alongside a Postgres container):

```
docker-compose up --build
```

Note: `docker-compose.yml` sets `SPRING_DATASOURCE_*` env vars that override `application.properties` (different DB name/credentials — compose uses `mvcendtoend`/`ashokit`/`ashokit`, the properties file defaults to `mydb`/`admin`/`admin`). The app listens on port `9091` in both local and Docker setups.

## Architecture

Classic layered Spring MVC, no service layer — controllers call the repository directly:

```
Browser ⇄ Controller ⇄ UserRepository (Spring Data CrudRepository) ⇄ PostgreSQL
              │
      User (form DTO, model/) vs UserEntity (JPA entity, entity/)
              │
       Thymeleaf templates (src/main/resources/templates/)
```

- **`model/User`** — plain DTO used only for form binding/validation (`@NotEmpty`/`@NotBlank` on name, email, phone). Never persisted directly.
- **`entity/UserEntity`** — `@Entity` mapped to the `users` table (`userid` PK, auto-generated identity). Uses Lombok `@Getter`/`@Setter`.
- **`repository/UserRepository`** — `CrudRepository<UserEntity, Integer>`, no custom queries.
- Controllers manually copy fields between `User` and `UserEntity` in every handler (no mapper/service layer exists).

### Two parallel, overlapping controllers

The app has two independent CRUD flows for the same `users` table — not a shared abstraction, genuinely duplicated:

- **`UserController`** (mounted at `/`) — the fuller flow: list (`GET /`), create/update (`POST /saveUser`, branches on whether `userID` is set), edit (`GET /editUser/{id}`), delete (`GET /deleteUser/{id}`). Renders `index.html` (Bootstrap-styled).
- **`UserMgmtController`** (mounted at `/userMgmt`) — an earlier/simpler add-only flow: list (`GET /userMgmt/`), add (`POST /userMgmt/addUser`, no update/delete). Renders `userMgmt.html`/`error.html` (plain table markup, "Ashok IT" branded — looks tutorial-derived).

When making CRUD changes, check whether both controllers need updating or whether the change only applies to the active (`UserController`) flow. `deleteUser`/`editUser` are implemented as `GET` endpoints (not idiomatic REST, but intentional given the current simple form-link UI).

### Config

`application.properties`: port `9091`, Postgres at `localhost:5432/mydb`, `spring.jpa.hibernate.ddl-auto=update` (schema auto-migrates from entities — no manual migrations), `spring.thymeleaf.cache=false` for dev.

Stray files to be aware of (not used by the build): `pom.xml.bak`, `application.properties.bak`, `src/main/resources/templates/test.json`.
