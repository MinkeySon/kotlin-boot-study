# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A personal study project for learning Kotlin with Spring Boot (README is in Korean, documenting Java→Kotlin differences). Most of the application layers are intentionally scaffolded/empty — `UserController`, `UserService`, and `UserServiceImpl` are placeholders waiting to be filled in. The only fully wired vertical slice is the QueryDSL custom repository (`userList()`).

## Commands

```bash
./gradlew build              # compile + run tests (also runs kapt to generate Q-classes)
./gradlew bootRun            # run the app on port 8080
./gradlew test               # run all tests
./gradlew test --tests "com.example.demo.DemoApplicationTests"   # run a single test class
./gradlew kaptKotlin         # regenerate QueryDSL Q-classes only
```

Swagger UI is available at `http://localhost:8080/swagger-ui.html` when running.

## Runtime prerequisites

The app requires a local **PostgreSQL** instance — connection details are hardcoded in [application.yml](src/main/resources/application.yml) (`localhost:5432/test_db`, user `postgres`). Both `bootRun` and `@SpringBootTest` (`DemoApplicationTests.contextLoads`) need this DB reachable or they fail at startup. `ddl-auto: update` means Hibernate creates/alters tables automatically.

## Stack & versions

Spring Boot 4.1.0, Kotlin 2.3.21 (JVM toolchain 17), Gradle 9.5.1. Note these are pre-release/bleeding-edge versions — the Spring Boot 4.x starters use new artifact names (e.g. `spring-boot-starter-webmvc` rather than `-web`), so don't assume Boot 3.x conventions when adding dependencies.

## Architecture

Standard layered structure under `com.example.demo`:

- `controller/` → `service/` (interface) → `service/impl/` (implementation) → `data/repository/` → `data/domain/` (entities)
- `data/dto/` holds request/response DTOs as Kotlin `data class`es
- `config/` holds `@Configuration` beans

**QueryDSL is the key non-obvious piece.** Custom queries follow a three-file pattern:
1. `UserRepository` extends both `JpaRepository<User, Long>` **and** `UserRepositoryCustom`
2. `UserRepositoryCustom` declares the custom method signatures
3. `UserRepositoryCustomImpl` (`@Repository`) implements them using the injected `JPAQueryFactory` bean (provided by [QueryDslConfig.kt](src/main/kotlin/com/example/demo/config/QueryDslConfig.kt))

The `JPAQueryFactory` uses generated `Q`-classes (e.g. `QUser`). These are produced by **kapt** from `@Entity` classes during compilation — after changing an entity, run a Gradle build before `QUser`-style references resolve. Generated sources live under `build/generated/`.

## Entity conventions

- Entities use the Kotlin **constructor-property** style: mapped columns go in the primary constructor `(...)`, while auto-generated fields (like `@Id @GeneratedValue uid`) go in the class `{ }` body. See [User.kt](src/main/kotlin/com/example/demo/data/domain/User.kt).
- The `kotlin("plugin.jpa")` + `allOpen` config in `build.gradle.kts` opens `@Entity`/`@MappedSuperclass`/`@Embeddable` classes so JPA can proxy them — entities are written as normal `class` (not `open class`).
- `BaseTimeEntity` (`@MappedSuperclass`) provides `createdAt`/`updatedAt`. It is annotated with `@EntityListeners(AuditingEntityListener)`, **but** timestamps are actually populated manually via `@PrePersist`/`@PreUpdate` — Spring Data auditing is *not* enabled (`@EnableJpaAuditing` is absent from the app class).

## Mixed Kotlin/Lombok note

Lombok is on the classpath and some entities carry Lombok annotations (`@ToString`, `@Getter`). These are largely redundant in Kotlin and Lombok's annotation processing does not compose cleanly with kapt — prefer Kotlin idioms (`data class`, property accessors) for new code rather than adding more Lombok.
