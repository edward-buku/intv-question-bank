package com.bukuwarung.interview.repayment.domain.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Loan aggregate: owns its dues and its status, and knows how to fold an allocation back into
 * its own state. This is the domain's job, NOT the service's or the persistence layer's.
 */
public class Loan {

  private final String id;
  private LoanStatus status;
  private List<Due> dues;

  public Loan(String id, LoanStatus status, List<Due> dues) {
    this.id = Objects.requireNonNull(id, "id");
    this.status = Objects.requireNonNull(status, "status");
    this.dues = new ArrayList<>(Objects.requireNonNull(dues, "dues"));
  }

  public String getId() {
    return id;
  }

  public LoanStatus getStatus() {
    return status;
  }

  /** Remaining (post-allocation) dues. Copy — callers cannot mutate internal state. */
  public List<Due> getDues() {
    return List.copyOf(dues);
  }

  /**
   * Reduce each due by the amount allocated to it, then recompute status:
   * nothing left owing -> CLOSED; money was applied but balance remains -> PARTIALLY_PAID;
   * nothing applied -> status unchanged.
   */
  public void applyAllocation(AllocationResult result) {
    Map<String, DueAllocation> byDue = new HashMap<>();
    for (DueAllocation a : result.allocations()) {
      byDue.put(a.dueId(), a);
    }

    List<Due> updated = new ArrayList<>(dues.size());
    for (Due due : dues) {
      DueAllocation a = byDue.get(due.id());
      if (a == null) {
        updated.add(due);
        continue;
      }
      updated.add(new Due(
          due.id(),
          due.dueDate(),
          due.penalty() - a.penaltyPaid(),
          due.interest() - a.interestPaid(),
          due.principal() - a.principalPaid()));
    }
    this.dues = updated;

    long outstanding = updated.stream().mapToLong(Due::totalOutstanding).sum();
    if (outstanding == 0) {
      this.status = LoanStatus.CLOSED;
    } else if (result.totalAllocated() > 0) {
      this.status = LoanStatus.PARTIALLY_PAID;
    }
  }
}
