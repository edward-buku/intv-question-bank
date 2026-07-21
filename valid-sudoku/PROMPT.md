# Sudoku — two variants

This folder has two tiers. Pick per candidate / round.

## Tier 1 — Validate (warm-up)  → `ValidSudoku.java`

Given a partially-filled 9×9 board (`'1'`–`'9'` or `'.'`), decide whether it is currently valid
(no repeated digit in any row, column, or 3×3 box). Classic single-pass validation. Good as a
warm-up or for a lighter candidate.

## Tier 2 — Generate **and** Verify (the real problem)  → `SudokuKit.java`

Build both sides of the problem and prove they agree.

You implement three things:

1. **`isValid(char[][] board)`** — decide validity seeing **only the board**. No metadata, no hint
   about how it was produced.
2. **`generateValid(int blanks, Random rng)`** — return a board you *know* is valid (optionally with
   some cells blanked out).
3. **`generateInvalid(int blanks, Random rng)`** — return a board you *know* is invalid.

A **property-based harness** (provided) generates ~1000 boards, half meant-valid and half
meant-invalid, hands each to your `isValid` **without** the label, and asserts your verdict matches
the generator's ground truth every time. Target output: `ALL OK`.

### Why this is the interesting part

- The naive way to make a valid board is to backtrack-solve one from scratch. **Don't.** Start from
  a single known-valid complete grid and apply only **validity-preserving transforms** — relabel the
  nine digits, permute rows within a band, swap whole bands/stacks, transpose — then blank cells
  (removing clues can never *create* a conflict). Recognizing this is the core insight.
- Making an *invalid* board sounds trivial but must be **guaranteed** invalid: inject a conflict you
  control (e.g., two identical digits in the same row) and make sure later blanking can't erase it.
- The validator being **blind** is the point: it's a genuine independent check, not a lookup of how
  the board was built.

### What we're assessing (80% DSA)

- Correct, efficient `isValid` (single pass, O(1) extra space, correct box indexing).
- The constructive insight for valid-board generation (transforms over a seed vs. search) and its
  complexity vs. backtracking.
- Reasoning about *why* each generator path guarantees its ground truth — this is a proof, not a
  vibe.
- Testing mindset: property-based verification over a fixed seed beats a handful of hand-picked
  cases.

Ask about anything underspecified — boundary conventions and "what counts as valid for a partial
board" are fair game.
