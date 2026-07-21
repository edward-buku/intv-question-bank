/**
 * Sudoku Generate-and-Verify (augmented)
 *
 * REFERENCE SOLUTION.
 *
 * Two capabilities, deliberately decoupled:
 *
 *  1. GENERATOR  — produces a 9x9 board that is either valid or invalid, on demand. It knows the
 *     ground truth of what it produced, but does NOT reveal it.
 *  2. VALIDATOR  — given only a board (char[][]), decides validity. It has no knowledge of how the
 *     board was created.
 *
 * A property-based harness ties them together: generate N boards, hide their construction, run the
 * validator, and assert the verdict matches ground truth every time.
 *
 * Key idea for generating a VALID board without a backtracking search: start from one known-valid
 * seed grid and apply only VALIDITY-PRESERVING transforms (digit relabel, row swaps within a band,
 * band/stack swaps, transpose), then blank cells (removing clues can never create a conflict).
 * An INVALID board is a valid one with a guaranteed conflict injected (two equal digits in a row).
 */

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class SudokuKit {

  private static final int N = 9;
  private static final int B = 3;

  // ---------------------------------------------------------------------------------------------
  // VALIDATOR — sees only the board.
  // ---------------------------------------------------------------------------------------------
  static boolean isValid(char[][] board) {
    if (board == null || board.length != N) {
      return false;
    }
    boolean[][] rowSeen = new boolean[N][N];
    boolean[][] colSeen = new boolean[N][N];
    boolean[][] boxSeen = new boolean[N][N];

    for (int r = 0; r < N; r++) {
      if (board[r] == null || board[r].length != N) {
        return false;
      }
      for (int c = 0; c < N; c++) {
        char ch = board[r][c];
        if (ch == '.') {
          continue;
        }
        if (ch < '1' || ch > '9') {
          return false;
        }
        int d = ch - '1';
        int box = (r / B) * B + (c / B);
        if (rowSeen[r][d] || colSeen[c][d] || boxSeen[box][d]) {
          return false;
        }
        rowSeen[r][d] = colSeen[c][d] = boxSeen[box][d] = true;
      }
    }
    return true;
  }

  // ---------------------------------------------------------------------------------------------
  // GENERATOR
  // ---------------------------------------------------------------------------------------------

  /** A known-valid, fully solved seed grid via the standard shifted-pattern construction. */
  static char[][] baseSolution() {
    char[][] g = new char[N][N];
    for (int r = 0; r < N; r++) {
      for (int c = 0; c < N; c++) {
        g[r][c] = (char) ('1' + ((B * (r % B) + r / B + c) % N));
      }
    }
    return g;
  }

  /** A valid (possibly partial) board: shuffled seed with {@code blanks} clues removed. */
  static char[][] generateValid(int blanks, Random rng) {
    char[][] g = baseSolution();
    shuffle(g, rng);
    blank(g, blanks, rng, Set.of());
    return g;
  }

  /** An invalid board: a shuffled seed with a guaranteed row conflict that blanking cannot erase. */
  static char[][] generateInvalid(int blanks, Random rng) {
    char[][] g = baseSolution();
    shuffle(g, rng);

    int r = rng.nextInt(N);
    int c1 = rng.nextInt(N);
    int c2;
    do {
      c2 = rng.nextInt(N);
    } while (c2 == c1);
    char v = (char) ('1' + rng.nextInt(N));

    // Blank everywhere except the two cells that carry the conflict, then plant the conflict.
    Set<Long> protectedCells = Set.of(cellKey(r, c1), cellKey(r, c2));
    blank(g, blanks, rng, protectedCells);
    g[r][c1] = v;
    g[r][c2] = v;
    return g;
  }

  // ---- validity-preserving transforms ----

  private static void shuffle(char[][] g, Random rng) {
    relabelDigits(g, rng);
    for (int t = 0; t < 20; t++) {
      int band = rng.nextInt(B);
      swapRows(g, band * B + rng.nextInt(B), band * B + rng.nextInt(B));
      int stack = rng.nextInt(B);
      swapCols(g, stack * B + rng.nextInt(B), stack * B + rng.nextInt(B));
      swapBands(g, rng.nextInt(B), rng.nextInt(B));
      swapStacks(g, rng.nextInt(B), rng.nextInt(B));
    }
    if (rng.nextBoolean()) {
      transpose(g);
    }
  }

  private static void relabelDigits(char[][] g, Random rng) {
    int[] perm = new int[N];
    for (int i = 0; i < N; i++) {
      perm[i] = i;
    }
    for (int i = N - 1; i > 0; i--) {
      int j = rng.nextInt(i + 1);
      int tmp = perm[i];
      perm[i] = perm[j];
      perm[j] = tmp;
    }
    for (int r = 0; r < N; r++) {
      for (int c = 0; c < N; c++) {
        g[r][c] = (char) ('1' + perm[g[r][c] - '1']);
      }
    }
  }

  private static void swapRows(char[][] g, int r1, int r2) {
    char[] tmp = g[r1];
    g[r1] = g[r2];
    g[r2] = tmp;
  }

  private static void swapCols(char[][] g, int c1, int c2) {
    for (int r = 0; r < N; r++) {
      char tmp = g[r][c1];
      g[r][c1] = g[r][c2];
      g[r][c2] = tmp;
    }
  }

  private static void swapBands(char[][] g, int b1, int b2) {
    for (int i = 0; i < B; i++) {
      swapRows(g, b1 * B + i, b2 * B + i);
    }
  }

  private static void swapStacks(char[][] g, int s1, int s2) {
    for (int i = 0; i < B; i++) {
      swapCols(g, s1 * B + i, s2 * B + i);
    }
  }

  private static void transpose(char[][] g) {
    for (int r = 0; r < N; r++) {
      for (int c = r + 1; c < N; c++) {
        char tmp = g[r][c];
        g[r][c] = g[c][r];
        g[c][r] = tmp;
      }
    }
  }

  private static void blank(char[][] g, int count, Random rng, Set<Long> protectedCells) {
    int removed = 0;
    int guard = 0;
    while (removed < count && guard < 10_000) {
      guard++;
      int r = rng.nextInt(N);
      int c = rng.nextInt(N);
      if (protectedCells.contains(cellKey(r, c)) || g[r][c] == '.') {
        continue;
      }
      g[r][c] = '.';
      removed++;
    }
  }

  private static long cellKey(int r, int c) {
    return (long) r * N + c;
  }

  // ---------------------------------------------------------------------------------------------
  // PROPERTY-BASED HARNESS — the validator never learns how a board was built.
  // ---------------------------------------------------------------------------------------------
  public static void main(String[] args) {
    // Sanity: the seed grid must itself be a valid, fully-solved board.
    System.out.println("base seed valid & complete: " + isValid(baseSolution()));

    long seed = 42L;
    Random rng = new Random(seed);
    int trials = 1000;
    int pass = 0;
    int firstFailures = 0;

    for (int i = 0; i < trials; i++) {
      boolean expectedValid = rng.nextBoolean();
      char[][] board =
          expectedValid ? generateValid(rng.nextInt(40), rng) : generateInvalid(rng.nextInt(30), rng);

      boolean verdict = isValid(board); // blind — only the board is passed in

      if (verdict == expectedValid) {
        pass++;
      } else if (firstFailures < 3) {
        firstFailures++;
        System.out.println("MISMATCH expected=" + expectedValid + " got=" + verdict + ":");
        print(board);
      }
    }

    System.out.printf("%nseed=%d  trials=%d  passed=%d  %s%n", seed, trials, pass,
        pass == trials ? "ALL OK" : "FAILURES PRESENT");
  }

  private static void print(char[][] board) {
    for (char[] row : board) {
      System.out.println("  " + new String(row));
    }
  }
}
