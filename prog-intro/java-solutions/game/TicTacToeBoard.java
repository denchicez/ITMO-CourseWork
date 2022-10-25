package game;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class TicTacToeBoard implements Board, Position {
    private static final Map<Cell, Character> SYMBOLS = Map.of(
            Cell.X, 'X',
            Cell.O, 'O',
            Cell.I, '-',
            Cell.L, '|',
            Cell.E, '.'
    );
    public final int n;
    public final int m;
    public final int k;
    private final List<Cell> cellOnBoard = List.of(Cell.X, Cell.O, Cell.I, Cell.L, Cell.I);
    private final Cell[][] cells;
    private int emptyCell;

    public TicTacToeBoard(int n, int m, int k) {
        this.cells = new Cell[n][m];
        this.k = k;
        this.n = n;
        this.m = m;
        this.emptyCell = this.n * this.m;
        for (Cell[] row : cells) {
            Arrays.fill(row, Cell.E);
        }
    }

    @Override
    public Position getPosition() {
        return this;
    }

    @Override
    public Cell getCell(final int nowTurn) {
        return cellOnBoard.get(nowTurn);
    }

    public int counterVector(final Move move, int f, int s, final int nowTurn) {
        int answer = 0;
        for (int i = 0; i <= this.k; i++) {
            if (move.getRow() + f * i >= this.n || move.getRow() + f * i < 0 ||
                    move.getColumn() + s * i < 0 || move.getColumn() + s * i >= this.m) {
                break;
            }
            if (cells[move.getRow() + f * i][move.getColumn() + s * i] == cellOnBoard.get(nowTurn)) {
                answer += 1;
            } else {
                break;
            }
        }
        return answer;
    }

    public boolean youWin(final Move move, final int nowTurn) {
        int inRow = counterVector(move, 0, 1, nowTurn) + counterVector(move, 0, -1, nowTurn) - 1;
        int inDiag1 = counterVector(move, 1, 1, nowTurn) + counterVector(move, -1, -1, nowTurn) - 1;
        int inDiag2 = counterVector(move, 1, 0, nowTurn) + counterVector(move, -1, 0, nowTurn) - 1;
        int inColumn = counterVector(move, 1, 0, nowTurn) + counterVector(move, -1, 0, nowTurn) - 1;
        if (inRow >= this.k || inDiag1 >= this.k || inDiag2 >= this.k || inColumn >= this.k) {
            return true;
        }
        return false;
    }

    @Override
    public Result makeMove(final Move move, final int nowTurn) {
        if (!isValid(move)) {
            return Result.LOSE;
        }
        cells[move.getRow()][move.getColumn()] = move.getValue();
        emptyCell--;
        boolean doYouWin = youWin(move, nowTurn);
        if (doYouWin) {
            return Result.WIN;
        }
        if (this.emptyCell == 0) {
            return Result.DRAW;
        }
        return Result.UNKNOWN;
    }

    @Override
    public boolean isValid(final Move move) {
        return 0 <= move.getRow() && move.getRow() < this.n
                && 0 <= move.getColumn() && move.getColumn() < this.m
                && cells[move.getRow()][move.getColumn()] == Cell.E;
    }

    @Override
    public int getN() {
        return this.n;
    }

    @Override
    public int getM() {
        return this.m;
    }

    @Override
    public Cell getCell(final int r, final int c) {
        return cells[r][c];
    }

    @Override
    public String toString() {
        int whiteSpaceCounterN = String.valueOf(this.n).length();
        int whiteSpaceCounterM = String.valueOf(this.m).length();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < whiteSpaceCounterN; i++) {
            sb.append(" ");
        }
        for (int r = 0; r < this.m; r++) {
            sb.append(r);
            for (int i = 0; i < whiteSpaceCounterM; i++) {
                sb.append(" ");
            }
        }
        for (int r = 0; r < this.n; r++) {
            sb.append("\n");
            sb.append(r);
            for (int i = 0; i < whiteSpaceCounterN - String.valueOf(r).length(); i++) {
                sb.append(" ");
            }
            for (int c = 0; c < this.m; c++) {
                sb.append(SYMBOLS.get(cells[r][c]));
                for (int i = 0; i < whiteSpaceCounterM + (String.valueOf(c).length() - 1); i++) {
                    sb.append(" ");
                }
            }
        }
        return sb.toString();
    }
}
