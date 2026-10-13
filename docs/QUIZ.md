# Quiz and interview kit

Answer without looking first. Score each answer 0 (wrong), 1 (partial), or 2 (clear with a code example). Target 16/20 before presenting the project in an interview.

## Ten questions

1. Trace a POST request from React to a committed reservation. Name each layer.
2. What race occurs if the stock-row lock is removed?
3. Why must subtraction and insertion share a transaction?
4. Why is a request key useful after a network timeout?
5. What happens when the same key has a different quantity?
6. Can two requests for different items reuse one global key successfully?
7. Why is disabling a viewer's button insufficient security?
8. Why does Basic authentication still need HTTPS and CSRF protection?
9. What do the concurrent tests prove, and what do they not prove?
10. How would you safely reserve two different components in one request?

<details><summary>Answer key</summary>

1. State -> fetch -> Vite proxy -> security -> validation/controller -> transactional service -> JPA -> database.
2. Both requests can read an old count and oversell or lose an update.
3. A failed reservation insert must also undo the stock change.
4. A retry returns the prior result instead of repeating the effect.
5. 409 conflict; stock remains unchanged.
6. No. The primary key permits one. A loser rolls back its transaction; retries with changed payload conflict.
7. Callers can bypass JavaScript. Server role checks are mandatory.
8. Basic encodes, not encrypts, credentials. Browsers may automatically resend them; tokens protect writes.
9. Correctness under the tested H2 schedule and fixture, not production throughput, arbitrary schedules, or PostgreSQL behavior.
10. Lock items in a consistent ID order, validate all availability, and commit all reservations atomically. Also test deadlocks and partial failures.
</details>

## Mock interview ladder

- Explain the project in 60 seconds without saying "production-grade".
- Show the exact line that prevents overselling.
- Remove the lock and demonstrate a failing test. Is the failure deterministic?
- Explain a conflicting-key rollback step by step.
- Compare pessimistic and optimistic locking for a hot inventory item.
- Discuss scoped keys, retention, logging without passwords, and auditability.
- Explain why procurement and Kafka are not present in this V1.

## Hands-on understanding checks

1. Add a manager-only restock endpoint with positive quantity validation and tests. Explain concurrent restock/reservation behavior.
2. Add a cancellation state without deleting the key, so replay cannot reserve twice. Test repeated cancellation.
3. Add PostgreSQL integration tests using Testcontainers. Report any H2/PostgreSQL differences.
4. Add an approval workflow as a separate increment, with an explicit state diagram and forbidden-transition tests.

An interviewer can ask one question at a time, follow weak answers with "show me in the code", and request a small change. Do not publish learning percentages without her answers.
