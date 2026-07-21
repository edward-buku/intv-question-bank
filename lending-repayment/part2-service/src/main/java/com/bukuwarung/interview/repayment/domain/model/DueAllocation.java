package com.bukuwarung.interview.repayment.domain.model;

/** How much of a single due each component received from one payment. */
public record DueAllocation(String dueId, long penaltyPaid, long interestPaid, long principalPaid) {

  public long total() {
    return penaltyPaid + interestPaid + principalPaid;
  }
}
