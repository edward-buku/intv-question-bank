package com.bukuwarung.interview.repayment.domain.model;

import java.time.Instant;

/** Domain event emitted once a repayment has been durably applied to a loan. */
public record RepaymentApplied(
    String loanId,
    String paymentReference,
    long totalAllocated,
    long excess,
    LoanStatus newStatus,
    Instant occurredAt) {}
