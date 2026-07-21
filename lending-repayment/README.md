# ⚠️ PARKED — Lending clean-architecture track (NOT for the payment DSA screen)

**Status: incomplete, parked intentionally. Do not use for the payment-role Round 1.**

The payment/SDE-II Round 1 is weighted **80% DSA / 10% Java / 10% resume**, so the primary
screen is a data-structures-&-algorithms problem (see `flame-graph-analyzer/` and
`valid-sudoku/`). This folder is a *different* instrument and is kept only because it fits a
higher/adjacent bar.

## What this is

A cohesive two-part **lending** exercise that tests the things a pure DSA problem cannot —
clean/modular design, hexagonal separation (domain vs adapters), idempotency, loan state
transitions, and partial-failure handling. It maps to:

- the **SSE** level (one above SDE II), and/or
- the SDE-II Round-1 *clean-architecture* rubric (hexagonal, idempotency, async).

## Contents

- `part1-allocation/` — **complete & verified.** Pure-function "Repayment Allocation" (waterfall
  across penalty → interest → principal, oldest due first; excess handling). Starter + reference
  (`solution/`) + PROMPT.md. Compiles and runs on Java 17.
- `part2-service/` — **incomplete scaffold.** A Spring Boot + H2 hexagonal service around the
  Part 1 logic: idempotent repayment processing keyed on `paymentReference`, loan state machine,
  domain event emission. Domain model, ports, and Spring wiring are stubbed in; the persistence
  adapters, web layer, controller, seeder, and the candidate-fill `RepaymentServiceImpl` +
  reference solution are **not finished**. Do not hand this out until completed and made to boot
  (`mvn spring-boot:run`).

Resume this only when staffing an SSE loop or a clean-architecture round — not for the DSA screen.
