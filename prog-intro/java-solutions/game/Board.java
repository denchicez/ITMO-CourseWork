package game;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Board {
    Position getPosition();

    Cell getCell(int nowTurn);

    Result makeMove(Move move, int playerSz);
}
