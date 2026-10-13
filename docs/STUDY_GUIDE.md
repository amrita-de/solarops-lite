# SolarOps Lite study guide

Start with a 45-minute code walkthrough, then build the changes in the quiz. This is study material for new Spring/JPA concepts, not evidence that you already master them.

## 1. Spring Boot and dependency injection

Boot assembles and starts the application. `@RestController` turns method results into HTTP responses. The controller receives a service and repository through its constructor. Spring provides those objects; do not create a repository with `new`.

Read `Application.java`, then `InventoryController.java`. Trace one reservation from request body to service call. Java records hold input values; Bean Validation checks them before the controller proceeds.

- [Official REST guide](https://spring.io/guides/gs/rest-service)
- [Amigoscode introduction video](https://www.youtube.com/watch?v=9SGDpanrc8U). Good for the controller/service/data-access idea; it is older, so use this project's Boot 3 configuration, not copied legacy security code.

## 2. JPA and SQL

An entity maps Java fields to a table. `StockItem` maps to `stock_item`; `Reservation.requestKey` is the primary key. JPA repositories supply lookup and save operations. Foreign keys stop reservations referencing missing components. SQL checks are a second defense against invalid quantities.

Use `schema.sql` to explain all constraints. Public entity fields keep this tiny demo short; a larger project should use encapsulated entities and response DTOs.

- [Spring Data JPA reference](https://docs.spring.io/spring-data/jpa/reference/jpa/locking.html)
- [PetClinic reference repository](https://github.com/spring-projects/spring-petclinic)

## 3. Transactions and concurrency

Without a lock, two callers could both read the same stock count and reserve it. `PESSIMISTIC_WRITE` locks the selected item until the transaction ends. `@Transactional` groups stock subtraction and reservation insertion. Either both commit, or both roll back.

The lock is in the database, not a Java `synchronized` block. A Java lock would not protect several server instances. Locks also have costs: contention, possible timeouts and deadlocks. This demo has one-item allocations, avoiding multi-item lock ordering. H2 tests are not proof about every database.

- [Declarative transactions](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html)
- [JPA locking](https://docs.spring.io/spring-data/jpa/reference/jpa/locking.html)

## 4. Idempotency

Networks fail after a server commits. A client may retry without knowing whether the first request succeeded. The request key lets the server return the original reservation without a second subtraction. Same key plus a different payload is a conflict, not a new reservation.

Find the `prior` branch in `InventoryService`. Explain why the unique primary key is still needed even though the code checks first. Competing requests for different items do not hold the same stock lock, so uniqueness protects the final insert.

## 5. Authentication, authorization and CSRF

Authentication answers who the caller is. Authorization answers whether the caller may reserve stock. A viewer can read but cannot write. BCrypt hashes the configured demo passwords. Basic authentication requires HTTPS outside localhost.

CSRF protection matters even with Basic authentication because browsers can retain and resend credentials. The UI obtains a token from `/api/csrf` and sends it in the write header. A missing token fails before business logic. Do not disable CSRF simply because an API is stateless.

- [Spring Security HTTP Basic](https://docs.spring.io/spring-security/reference/servlet/authentication/passwords/basic.html)

## 6. React and the API boundary

Component state holds the form and inventory. A successful write is followed by a reload. Errors are shown to the user; the UI is not the authority for permissions. The server still enforces the viewer restriction even if a caller bypasses a disabled button.

- [React learning path](https://react.dev/learn)

## 7. Testing and evidence

Service tests use the actual SQL persistence layer rather than mocks. The concurrent test schedules 20 callers for 10 units and checks exactly 10 successful allocations. MockMvc tests inspect security and input validation at the HTTP layer. A browser test checks the wiring beyond the API.

Change one behavior, write a failing test, then fix it. Be ready to describe test limitations: H2 differs from PostgreSQL, the fixture is small, and no load benchmark has been run.

## Honest interview introduction

"This is an AI-assisted portfolio build that I studied and extended. It models a solar inventory problem, independent of my employer's code. I can demonstrate the stock lock, transaction rollback, request replay and role checks. Here are the tests, and here is what I changed myself."

Use that only after you really can do the walkthrough. Record your own changes in `LEARNING_LOG.md`; do not invent commits or personal ownership history.
