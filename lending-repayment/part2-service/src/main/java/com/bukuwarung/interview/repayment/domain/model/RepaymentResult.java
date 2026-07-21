package com.bukuwarung.interview.repayment.domain.model;

import java.util.List;

/** The outcome of applying a repayment — returned to the caller and cached for idempotency. */
public record RepaymentResult(
    String loanId,
    String paymentReference,
    long totalAllocated,
    long excess,
    LoanStatus loanStatus,
    List<DueAllocation> allocations) {

  public RepaymentResult {
    allocations = List.copyOf(allocations);
  }
}
