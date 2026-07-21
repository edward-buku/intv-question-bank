/**
 * Sudoku Generate-and-Verify (augmented) — starter.
 *
 * Read PROMPT.md first. You will implement THREE things:
 *
 *  1. isValid(board)         — decide if a board is valid, seeing ONLY the board.
 *  2. generateValid(...)     — produce a board you KNOW is valid.
 *  3. generateInvalid(...)   — produce a board you KNOW is invalid.
 *
 * The provided harness in main() generates boards, hides how they were built, runs your validator,
 * and checks the verdict against ground truth. Make it print ALL OK.
 *
 * Hint: don't search/backtrack to build a valid board. Think about a known-valid starting grid and
 * transforms that cannot break validity.
 *
 * You can search the internet for Java library references, but don't rely on AI for the solution
 * (use '-ai' flag when searching).
 */

import java.util.*;

public class SudokuKitStarterCode {

  private static final int N = 9;

  static boolean isValid(char[][] board) {
    // TODO: implement — sees only the board
    throw new UnsupportedOperationException("not implemented");
  }

  static char[][] generateValid(int blanks, Random rng) {
    // TODO: implement — return a board that is definitely valid
    throw new UnsupportedOperationException("not implemented");
  }

  static char[][] generateInvalid(int blanks, Random rng) {
    // TODO: implement — return a board that is definitely invalid
    throw new UnsupportedOperationException("not implemented");
  }

  // ---- provided property-based harness: do not change ----
  public static void main(String[] args) {
    long seed = 42L;
    Random rng = new Random(seed);
    int trials = 1000;
    int pass = 0;

    for (int i = 0; i < trials; i++) {
      boolean expectedValid = rng.nextBoolean();
      char[][] board =
          expectedValid ? generateValid(rng.nextInt(40), rng) : generateInvalid(rng.nextInt(30), rng);
      boolean verdict = isValid(board); // blind — only the board is passed in
      if (verdict == expectedValid) {
        pass++;
      }
    }
    System.out.printf("seed=%d  trials=%d  passed=%d  %s%n", seed, trials, pass,
        pass == trials ? "ALL OK" : "FAILURES PRESENT");
  }
}
