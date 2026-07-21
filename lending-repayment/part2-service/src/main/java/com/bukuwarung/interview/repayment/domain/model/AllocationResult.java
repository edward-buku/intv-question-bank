package com.bukuwarung.interview.repayment.domain.model;

import java.util.List;

/** Outcome of allocating one payment across a set of dues. */
public record AllocationResult(List<DueAllocation> allocations, long totalAllocated, long excess) {

  public AllocationResult {
    allocations = List.copyOf(allocations);
  }
}
