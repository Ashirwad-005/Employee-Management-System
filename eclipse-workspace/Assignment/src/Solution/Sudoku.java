package Solution;

import java.util.Scanner;

public class Sudoku {

    public static void main(String[] args) {
        int[][] board = new int[9][9];
        System.out.println("Enter the values of the Sudoku to be solved (9x9 grid):");
        System.out.println("Enter values from 1-9");
        System.out.println("Enter the value 0 to fill the value by the program or to be solved by the system");
        Scanner scan = new Scanner(System.in);
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.println("Enter the value for board[" + i + "][" + j + "]");
                board[i][j] = scan.nextInt();
            }
        }
        System.out.println("Input Sudoku Board:");
        printBoard(board);
        if (help(board, 0, 0)) {
            System.out.println("Solved Sudoku Board:");
            printBoard(board);
        } else {
            System.out.println("No solution exists for the given Sudoku board.");
        }
    }

    public static void printBoard(int[][] board) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static boolean safe(int[][] board, int row, int col, int number) {
        for (int i = 0; i < board.length; i++) {
            if (board[i][col] == number) {
                return false;
            }
            if (board[row][i] == number) {
                return false;
            }
        }
        int sr = (row / 3) * 3;
        int sc = (col / 3) * 3;
        for (int i = sr; i < sr + 3; i++) {
            for (int j = sc; j < sc + 3; j++) {
                if (board[i][j] == number) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean help(int[][] board, int row, int col) {
        if (row == board.length) {
            return true;
        }
        int nrow = 0;
        int ncol = 0;
        if (col != board.length - 1) {
            nrow = row;
            ncol = col + 1;
        } else {
            nrow = row + 1;
            ncol = 0;
        }
        if (board[row][col] != 0) {
            if (help(board, nrow, ncol)) {
                return true;
            }
        } else {
            for (int i = 1; i <= 9; i++) {
                if (safe(board, row, col, i)) {
                    board[row][col] = i;
                    if (help(board, nrow, ncol)) {
                        return true;
                    } else {
                        board[row][col] = 0;
                    }
                }
            }
        }
        return false;
    }
}
