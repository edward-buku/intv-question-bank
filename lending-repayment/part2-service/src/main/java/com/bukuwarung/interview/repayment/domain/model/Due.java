package com.bukuwarung.interview.repayment.domain.model;

import java.time.LocalDate;
import java.util.Objects;

/** An outstanding due with per-component balances (whole rupiah). Immutable. */
public record Due(String id, LocalDate dueDate, long penalty, long interest, long principal) {

  public Due {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(dueDate, "dueDate");
    if (penalty < 0 || interest < 0 || principal < 0) {
      throw new IllegalArgumentException("component balances must be non-negative for due " + id);
    }
  }

  public long totalOutstanding() {
    return penalty + interest + principal;
  }
}
