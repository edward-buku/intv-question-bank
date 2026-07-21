import java.time.LocalDate;
import java.util.*;

/**
 * PART 1 — Repayment Allocation (starter).
 *
 * Read PROMPT.md first. Implement RepaymentAllocator.allocate(...).
 *
 * You are given a `Due` class below. Review it and make any changes you think are
 * necessary for it to be a well-behaved, safe Java domain object before you build your
 * logic. You may add classes / methods / fields as needed.
 *
 * Amounts are whole rupiah (no cents). Uncomment test cases in main() as you go.
 */
class Due {
  String id;
  LocalDate dueDate;
  long penalty;
  long interest;
  long principal;

  Due(String id, LocalDate dueDate, long penalty, long interest, long principal) {
    this.id = id;
    this.dueDate = dueDate;
    this.penalty = penalty;
    this.interest = interest;
    this.principal = principal;
  }
}

class RepaymentAllocator {

  /**
   * Allocate `payment` across `dues` and return, per due, how much went to each component,
   * plus the total allocated and any excess left over.
   */
  Object allocate(List<Due> dues, long payment) {
    // TODO: implement
    throw new UnsupportedOperationException("not implemented");
  }
}

public class RepaymentAllocatorStarter {
  public static void main(String[] args) {
    RepaymentAllocator allocator = new RepaymentAllocator();

    // Start with the worked example, then handle the anomalies described in PROMPT.md.
    List<Due> dues = new ArrayList<>();
    dues.add(new Due("D1", LocalDate.parse("2026-01-01"), 100, 200, 700));
    System.out.println(allocator.allocate(dues, 1_000)); // expect: D1 fully paid, excess 0

    // allocator.allocate(dues, 1_500);  // overpayment -> excess 500
    // allocator.allocate(dues, 40);     // partial -> only part of penalty paid
    // allocator.allocate(dues, 0);      // no-op
    // allocator.allocate(new ArrayList<>(), 500); // empty dues -> all excess
  }
}
