# Part 1 — Repayment Allocation (≈20 min)

## Background

In our lending system, a borrower makes a repayment against their loan. The loan has one or
more **outstanding dues** (think: monthly installments that are due or past due). Each due is
split into three components:

- **penalty** — late fees
- **interest**
- **principal**

When money comes in, we must decide exactly how much of it settles each component of each due.
Getting this wrong means a borrower's balance is incorrect — so correctness is everything.

## Your task

Implement `RepaymentAllocator.allocate(List<Due> dues, long payment)`.

Given a borrower's outstanding dues and a single incoming `payment` (whole rupiah), allocate the
payment and return, for each due, **how much was applied to penalty, interest, and principal**,
plus the **total allocated** and any **excess** (money left over after everything is settled).

**Allocation rules**

1. Settle dues **oldest first** (by due date). Dues may arrive unsorted.
2. Within a single due, settle in the order **penalty → interest → principal**.
3. A component can never receive more than it owes; move to the next once it's cleared.
4. If money remains after **all** dues are fully settled, return it as `excess`.

**Worked example**

```
dues:    D1 (2026-01-01)  penalty=100  interest=200  principal=700
payment: 1000
=> D1: penalty=100, interest=200, principal=700 | totalAllocated=1000, excess=0

payment: 250 (same due)
=> D1: penalty=100, interest=150, principal=0   | totalAllocated=250,  excess=0
```

## Constraints & expectations

- You are given a `Due` class. **Review it and harden it** into a proper, safe domain object
  before building your logic (immutability, validation, null-safety — your call, be ready to
  justify it).
- Design the return type yourself — model it cleanly (don't return a raw map or a stringly-typed
  blob).
- **Handle data anomalies gracefully.** Think about what inputs could break this and decide how
  each should behave.
- Money is whole rupiah. Choose your numeric type deliberately.
- Clean, modular, testable code. Extract helpers where it reads better.

Ask about anything underspecified — some of it is deliberately left open.
