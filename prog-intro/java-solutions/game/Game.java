package game;

import java.util.Arrays;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class Game {
    private final boolean log;
    private final Player[] players;
    private final int playerSize;
    private boolean[] checks;
    private int counterDiedPlayers = 0;
    public Game(final boolean log, final Player... players) {
        this.log = log;
        this.players = players;
        this.playerSize = this.players.length;
        this.checks = new boolean[this.playerSize];
        Arrays.fill(checks, false);
    }

    public int play(Board board) {
        int nowMove = 0;
        while (true) {
            if (checks[nowMove] == true) {
                nowMove++;
                nowMove = nowMove % playerSize;
                continue;
            }
            if(counterDiedPlayers==playerSize-1){
                log("Player " + nowMove + " won");
                return nowMove;
            }
            final int result = move(board, this.players[nowMove], nowMove);
            if (result > 0) { // -1 and -404 is continue
                return result;
            }
            if (result == -404) {
                checks[nowMove] = true;
                counterDiedPlayers++;
            }
            nowMove++;
            nowMove = nowMove % playerSize;
        }
    }

    private int move(final Board board, final Player player, final int nowTurn) {
        try {
            final Move move = player.move(board.getPosition(), board.getCell(nowTurn));
            final Result result = board.makeMove(move, nowTurn);
            log("Player " + nowTurn + " move: " + move);
            log("Position:\n" + board);
            if (result == Result.WIN) {
                log("Player " + nowTurn + " won");
                return nowTurn;
            } else if (result == Result.LOSE) {
                log("Player " + nowTurn + " lose");
                return -404; // Undifined Behavior
            } else if (result == Result.DRAW) {
                log("Draw");
                return 0;
            } else {
                return -1;
            }
        } catch (Exception exception){
            System.err.println("YOU ARE BAD LOSER");
            return -404;
        }
    }

    private void log(final String message) {
        if (log) {
            System.out.println(message);
        }
    }
}
