package game;

import java.io.PrintStream;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class HumanPlayer implements Player {
    private final PrintStream out;
    private Scanner in;

    public HumanPlayer(final PrintStream out, final Scanner in) {
        this.out = out;
        this.in = in;
    }

    public HumanPlayer() {
        this(System.out, new Scanner(System.in));
    }

    @Override
    public Move move(final Position position, final Cell cell) {
        while (true) {
            out.println("Position");
            out.println(position);
            out.println(cell + "'s move");
            out.println("Enter row and column");
            while (true) {
                try {
                    final Move move = new Move(in.nextInt(), in.nextInt(), cell);
                    if (position.isValid(move)) {
                        return move;
                    }
                    System.err.println("This move isn't correct");
                } catch (InputMismatchException e) {
                    System.err.println(e);
                    System.err.println("You must insert only two numbers");
                    in = new Scanner(System.in);
                } catch (NoSuchElementException e) {
                    System.err.println(e);
                    System.err.println("Problem with input. I think you want clost it, but you must play");
                    in = new Scanner(System.in);
                }
            }
        }
    }
}
