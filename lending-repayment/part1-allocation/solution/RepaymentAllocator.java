package solution;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * REFERENCE SOLUTION — Part 1: Repayment Allocation.
 *
 * <p>Allocates a single incoming payment across a borrower's outstanding dues using a
 * deterministic waterfall:
 *
 * <ol>
 *   <li>Dues are settled oldest-first (by due date, then id as a stable tie-break).
 *   <li>Within a due, components are settled penalty -> interest -> principal.
 *   <li>Any amount left once every due is fully settled is returned as {@code excess}
 *       (this is the hook that a real system would sweep back to the borrower, cf. the
 *       over-repayment excess sweep in production).
 * </ol>
 *
 * <p>All amounts are whole rupiah held as {@code long} — never floating point. Money in a
 * lending system must be exact; a candidate reaching for {@code double} here is a red flag.
 */
final class RepaymentAllocator {

  AllocationResult allocate(List<Due> dues, long payment) {
    if (payment < 0) {
      throw new IllegalArgumentException("payment must be non-negative, got " + payment);
    }

    List<Due> ordered = new ArrayList<>(dues == null ? List.of() : dues);
    ordered.sort(Comparator.comparing(Due::getDueDate).thenComparing(Due::getId));

    List<DueAllocation> allocations = new ArrayList<>();
    long remaining = payment;

    for (Due due : ordered) {
      long toPenalty = take(remaining, due.getPenalty());
      remaining -= toPenalty;
      long toInterest = take(remaining, due.getInterest());
      remaining -= toInterest;
      long toPrincipal = take(remaining, due.getPrincipal());
      remaining -= toPrincipal;

      allocations.add(new DueAllocation(due.getId(), toPenalty, toInterest, toPrincipal));
    }

    long excess = remaining;
    return new AllocationResult(allocations, payment - excess, excess);
  }

  /** Amount that can be applied to a component: never more than is available or owed. */
  private static long take(long available, long owed) {
    return Math.min(available, owed);
  }
}

/** Immutable outstanding due with per-component balances (whole rupiah). */
final class Due {
  private final String id;
  private final LocalDate dueDate;
  private final long penalty;
  private final long interest;
  private final long principal;

  Due(String id, LocalDate dueDate, long penalty, long interest, long principal) {
    this.id = Objects.requireNonNull(id, "id");
    this.dueDate = Objects.requireNonNull(dueDate, "dueDate");
    if (penalty < 0 || interest < 0 || principal < 0) {
      throw new IllegalArgumentException("component balances must be non-negative for due " + id);
    }
    this.penalty = penalty;
    this.interest = interest;
    this.principal = principal;
  }

  String getId() { return id; }
  LocalDate getDueDate() { return dueDate; }
  long getPenalty() { return penalty; }
  long getInterest() { return interest; }
  long getPrincipal() { return principal; }
}

/** How much of a single due each component received. */
final class DueAllocation {
  private final String dueId;
  private final long penaltyPaid;
  private final long interestPaid;
  private final long principalPaid;

  DueAllocation(String dueId, long penaltyPaid, long interestPaid, long principalPaid) {
    this.dueId = dueId;
    this.penaltyPaid = penaltyPaid;
    this.interestPaid = interestPaid;
    this.principalPaid = principalPaid;
  }

  String getDueId() { return dueId; }
  long getPenaltyPaid() { return penaltyPaid; }
  long getInterestPaid() { return interestPaid; }
  long getPrincipalPaid() { return principalPaid; }
  long total() { return penaltyPaid + interestPaid + principalPaid; }

  @Override
  public String toString() {
    return String.format(
        "%s{penalty=%d, interest=%d, principal=%d}", dueId, penaltyPaid, interestPaid, principalPaid);
  }
}

/** Result of allocating one payment across the dues. */
final class AllocationResult {
  private final List<DueAllocation> allocations;
  private final long totalAllocated;
  private final long excess;

  AllocationResult(List<DueAllocation> allocations, long totalAllocated, long excess) {
    this.allocations = List.copyOf(allocations);
    this.totalAllocated = totalAllocated;
    this.excess = excess;
  }

  List<DueAllocation> getAllocations() { return allocations; }
  long getTotalAllocated() { return totalAllocated; }
  long getExcess() { return excess; }
}

/** Runs the reference solution against the hidden test cases so its output can be inspected. */
class RepaymentAllocatorDemo {

  public static void main(String[] args) {
    RepaymentAllocator allocator = new RepaymentAllocator();

    runCase(allocator, "Case 1 - exact settlement, no excess", payment(1_000), dues(
        due("D1", "2026-01-01", 100, 200, 700)));

    runCase(allocator, "Case 2 - overpayment leaves excess", payment(1_500), dues(
        due("D1", "2026-01-01", 100, 200, 700)));

    runCase(allocator, "Case 3 - partial: payment smaller than first penalty", payment(40), dues(
        due("D1", "2026-01-01", 100, 200, 700)));

    runCase(allocator, "Case 4 - zero payment is a no-op", payment(0), dues(
        due("D1", "2026-01-01", 100, 200, 700)));

    runCase(allocator, "Case 5 - empty dues, everything is excess", payment(500), dues());

    runCase(allocator, "Case 6 - unsorted dues settled oldest-first", payment(1_000), dues(
        due("D2", "2026-03-01", 0, 0, 500),
        due("D1", "2026-01-01", 100, 200, 300)));

    runCase(allocator, "Case 7 - mid-due exhaustion (penalty full, interest partial)", payment(250),
        dues(due("D1", "2026-01-01", 100, 200, 700)));

    System.out.println("\nCase 8 - negative payment is rejected");
    try {
      allocator.allocate(dues(due("D1", "2026-01-01", 0, 0, 100)), -50);
      System.out.println("  FAIL: expected IllegalArgumentException");
    } catch (IllegalArgumentException e) {
      System.out.println("  OK: " + e.getMessage());
    }
  }

  private static void runCase(RepaymentAllocator allocator, String title, long payment, List<Due> dues) {
    System.out.println("\n" + title + "  (payment=" + payment + ")");
    AllocationResult result = allocator.allocate(dues, payment);
    for (DueAllocation a : result.getAllocations()) {
      System.out.println("  " + a);
    }
    System.out.println("  totalAllocated=" + result.getTotalAllocated() + ", excess=" + result.getExcess());
  }

  private static long payment(long amount) { return amount; }
  private static List<Due> dues(Due... d) { return new ArrayList<>(List.of(d)); }
  private static Due due(String id, String date, long penalty, long interest, long principal) {
    return new Due(id, LocalDate.parse(date), penalty, interest, principal);
  }
}
