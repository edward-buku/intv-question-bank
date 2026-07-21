/**
 * Valid Sudoku (Board Validation)
 *
 * BACKGROUND:
 * A Sudoku board is a 9x9 grid. It is being filled in progressively, so most cells may still
 * be empty. We need to validate — at any point — that what has been filled so far does not
 * already violate the rules. We are NOT solving the board, only checking the current state.
 *
 * THE PROBLEM:
 * Given a partially filled 9x9 board, determine if it is currently valid. A board is valid when:
 *  1. Each row contains no repeated digit ('1'-'9').
 *  2. Each column contains no repeated digit.
 *  3. Each of the nine 3x3 sub-boxes contains no repeated digit.
 * Empty cells are represented by '.' and are ignored. Only the filled cells must satisfy the
 * rules; the board does not need to be solvable.
 *
 * EXAMPLE:
 *  5 3 . | . 7 . | . . .
 *  6 . . | 1 9 5 | . . .
 *  . 9 8 | . . . | . 6 .
 *  ------+-------+------
 *  ... (valid partial board) => true
 *
 * IMPLEMENTATION DETAILS & CONSTRAINTS:
 * - Input is a char[][] of size 9x9 containing '1'-'9' or '.'.
 * - Aim for a single pass and O(1) extra space (the grid is fixed-size 9x9).
 * - Be ready to discuss time/space complexity and how you map a cell to its 3x3 box.
 * - Handle a fully empty board and malformed input gracefully.
 * - You can search the internet for Java library references, however you can't rely on AI
 *   for answers or searching the solution directly (use '-ai' flag when searching).
 */

import java.util.HashSet;
import java.util.Set;

class ValidSudoku {

  boolean isValidSudoku(char[][] board) {
    if (board == null || board.length != 9) {
      return false;
    }

    Set<String> seen = new HashSet<>();
    for (int r = 0; r < 9; r++) {
      if (board[r] == null || board[r].length != 9) {
        return false;
      }
      for (int c = 0; c < 9; c++) {
        char value = board[r][c];
        if (value == '.') {
          continue;
        }
        if (value < '1' || value > '9') {
          return false; // malformed cell
        }
        int box = (r / 3) * 3 + (c / 3);
        // A digit is a violation if its row/column/box marker was already recorded.
        if (!seen.add("r" + r + "-" + value)
            || !seen.add("c" + c + "-" + value)
            || !seen.add("b" + box + "-" + value)) {
          return false;
        }
      }
    }
    return true;
  }

  public static void main(String[] args) {
    ValidSudoku validator = new ValidSudoku();

    run("Case 1 - valid partial board -> true", validator, board(
        "53..7....",
        "6..195...",
        ".98....6.",
        "8...6...3",
        "4..8.3..1",
        "7...2...6",
        ".6....28.",
        "...419..5",
        "....8..79"), true);

    run("Case 2 - duplicate in a 3x3 box (and column) -> false", validator, board(
        "83..7....",
        "6..195...",
        ".98....6.",
        "8...6...3",
        "4..8.3..1",
        "7...2...6",
        ".6....28.",
        "...419..5",
        "....8..79"), false);

    run("Case 3 - duplicate in a row -> false", validator, board(
        "553..7...", // two 5s in row 0
        "6..195...",
        ".98....6.",
        "8...6...3",
        "4..8.3..1",
        "7...2...6",
        ".6....28.",
        "...419..5",
        "....8..79"), false);

    run("Case 4 - duplicate in a column -> false", validator, board(
        "53..7....",
        "5..195...", // second 5 in column 0
        ".98....6.",
        "8...6...3",
        "4..8.3..1",
        "7...2...6",
        ".6....28.",
        "...419..5",
        "....8..79"), false);

    run("Case 5 - fully empty board -> true", validator, board(
        ".........",
        ".........",
        ".........",
        ".........",
        ".........",
        ".........",
        ".........",
        ".........",
        "........."), true);

    run("Case 6 - malformed input (null) -> false", validator, null, false);
  }

  private static void run(String title, ValidSudoku validator, char[][] board, boolean expected) {
    boolean actual = validator.isValidSudoku(board);
    System.out.printf("%-55s actual=%-5b %s%n", title, actual, actual == expected ? "OK" : "FAIL");
  }

  /** Build a 9x9 board from nine 9-char row strings. */
  private static char[][] board(String... rows) {
    char[][] grid = new char[rows.length][];
    for (int r = 0; r < rows.length; r++) {
      grid[r] = rows[r].toCharArray();
    }
    return grid;
  }
}
