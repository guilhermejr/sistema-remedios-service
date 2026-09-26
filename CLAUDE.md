# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

Medication tracking — stock, expiry dates, consumption and symptoms.

| | |
|---|---|
| Port | `9006` |
| Context path | `/remedios-service` |
| Role required | `ROLE_REMEDIOS` |

Part of a personal microservices system; sibling repos live at `../sistema-*`. The API gateway fronts it at `https://sistema-backend.guilhermejr.net/remedios-service`.

## Security comes from a library

There is no `config/security` package here. Everything — JWT validation, the filter, the security chains, `@EnableMethodSecurity` — arrives from `net.guilhermejr.sistema:seguranca-jwt` through Spring Boot **auto-configuration**. Declaring the dependency is all it takes.

Two consequences worth knowing:

- **Removing or failing to resolve that dependency does not break the build.** No code here references its classes, so the service still compiles and starts — with no security configuration at all, at which point Spring Boot's default kicks in and locks every route behind HTTP Basic with a generated password. The symptom is blanket 401s with a `WWW-Authenticate: Basic` header.
- **`@PreAuthorize` on the controllers only works because the library brings `@EnableMethodSecurity`.** Without it the annotations are silently ignored and any valid token reaches every endpoint regardless of role.

To override any piece, declare your own bean of the same type — or, for the chains, the same name (`filterChain`, `filterChainActuator`). Every bean in the library is `@ConditionalOnMissingBean`.

The signing key is `sistema.auth.jwtSecret`, shared across all services. It must be Base64 decoding to at least 64 bytes (HS512). A weak value fails at startup with an explicit message rather than being silently accepted.

## Every lookup by id is scoped to the owner

`RemedioService.remedioDoUsuario` and `SintomaService.sintomaDoUsuario` are the only
way an id reaches the database, and both go through `findByIdAndUsuario`. A record
belonging to someone else answers **404, not 403**, so the endpoint never reveals
that the id exists. `salvar` links only symptoms owned by the caller, so a medicine
cannot be attached to another user's symptom.

`consumir` keeps taking the user from the security context, but `inserir` on the
purchase side of the system is the opposite case — check how a scheduled task calls
a service before switching it to read the context.

Two endpoint semantics worth knowing:

- **`/remedios/vencendo` includes what already expired.** The query is
  `validade <= hoje + dias`, deliberately without a lower bound: a medicine that
  expired yesterday is more urgent than one expiring in 29 days, and the old
  `BETWEEN hoje AND ate` dropped it off the bottom of the range. Results come
  ordered by `validade` ascending.
- **Insufficient stock is a 400, not a 404.** `consumir` throws `ExceptionDefault`
  when `quantidade < dose`, and the message names the medicine and both numbers.
  It used to be `ExceptionNotFound` with a message that always said "menor ou igual
  a 0" regardless of the real cause.

## Database migrations

PostgreSQL with Flyway, migrations in `src/main/resources/db/migration`.

`spring-boot-flyway` is declared in `pom.xml` and **must stay there**. In Spring Boot 4 the Flyway auto-configuration moved into that separate module; with only `flyway-core` on the classpath the service starts normally, logs nothing, and applies no migrations at all.

## Actuator

`/actuator/health` is public and returns the status only (`show-details: never`, set in the shared config repo). Everything else under `/actuator/**` requires HTTP Basic with `ROLE_ACTUATOR`, whose credentials come from Vault.

## Configuration comes from outside

This service stores almost no configuration of its own. `application.yml` only bootstraps `spring.config.import`, which pulls from:

- **Vault** — `secret/application` (shared: `JWTSecret`, actuator credentials, mail, AWS) and `secret/<service-name>` (its own DB credentials)
- **Config Server** — `server.port`, `server.servlet.context-path`, datasource, JPA settings

Both must be reachable or the service will not start.

`VAULT_TOKEN` is required and **has no default**. Without it Spring sends the literal string `${VAULT_TOKEN}` to Vault, gets a 403 that Spring Cloud Vault swallows (`fail-fast` is off), and the startup fails much later with a misleading `${someProperty} is malformed`. If you are chasing a confusing startup error, check `VAULT_TOKEN` first.

## Building and running

Java **21 only**. The Homebrew default JDK on this machine is newer and will break the build:

```bash
export JAVA_HOME=/Users/guilhermejr/Library/Java/JavaVirtualMachines/openjdk-21.0.2/Contents/Home
./mvnw clean package
VAULT_TOKEN=<token> java -jar target/*.jar --spring.profiles.active=dev
```

Do not raise `java.version` past 21 while ModelMapper is a dependency — the ByteBuddy bundled in it cannot generate classes on JDK 24+, and the app dies building its mappers with an `UnsupportedOperationException` that does not name the real cause.

## Deploying

`git push origin main` **is** the deploy. A `post-receive` hook on the VPS checks out, runs `mvn clean package` inside a throwaway `maven:3.9-amazoncorretto-21` container, builds the image and restarts it via docker compose. There is no separate release step.

Because the build happens on the VPS, any dependency from a private repository needs credentials **there**, not locally — the hook mounts `/home/guilhermejr/.m2` and passes `GITHUB_TOKEN`.

The `Dockerfile` only copies a prebuilt jar; it carries a `HEALTHCHECK` that polls `/actuator/health`.
