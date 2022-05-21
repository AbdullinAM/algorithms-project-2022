import org.junit.jupiter.api.*;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    Random r = new Random();

    @Test
    public void reveal() {
        int cols = 10;
        int rows = 10;

        Game game = new Game(cols, rows, 20);
        int closedCellsAmount = game.getClosedCellsAmount();
        assertEquals(GameState.PLAYING, game.getState());

        Coord coord = new Coord(r.nextInt(cols), r.nextInt(rows));
        Cell cell = game.getHiddenBoard()[coord.getX()][coord.getY()];
        Cell[][] expected = game.getVisibleBoard();

        //left click num
        while (game.getVisibleBoard()[coord.getX()][coord.getY()] != Cell.CLOSED || cell == Cell.BOMB || cell == Cell.NUM0) {
            coord = new Coord(r.nextInt(cols), r.nextInt(rows));
            cell = game.getHiddenBoard()[coord.getX()][coord.getY()];
        }
        game.onLeftButtonPressed(coord);

        assertSame(game.getState(), GameState.PLAYING);
        expected[coord.getX()][coord.getY()] = cell;
        assertEquals(expected, game.getVisibleBoard());
        closedCellsAmount--;
        assertEquals(game.getClosedCellsAmount(), closedCellsAmount);

        //right click closed
        while (game.getVisibleBoard()[coord.getX()][coord.getY()] != Cell.CLOSED) {
            coord = new Coord(r.nextInt(cols), r.nextInt(rows));
        }
        game.onRightButtonPressed(coord);

        assertSame(game.getState(), GameState.PLAYING);
        expected[coord.getX()][coord.getY()] = Cell.FLAGGED;
        assertEquals(expected, game.getVisibleBoard());
        assertEquals(game.getClosedCellsAmount(), closedCellsAmount);

        //right click flagged
        game.onRightButtonPressed(coord);

        assertSame(game.getState(), GameState.PLAYING);
        expected[coord.getX()][coord.getY()] = Cell.CLOSED;
        assertEquals(expected, game.getVisibleBoard());
        assertEquals(game.getClosedCellsAmount(), closedCellsAmount);

        while (game.getVisibleBoard()[coord.getX()][coord.getY()] != Cell.CLOSED || cell != Cell.BOMB) {
            coord = new Coord(r.nextInt(cols), r.nextInt(rows));
            cell = game.getHiddenBoard()[coord.getX()][coord.getY()];
        }
        game.onLeftButtonPressed(coord);

        assertSame(game.getState(), GameState.LOSE);
        expected[coord.getX()][coord.getY()] = Cell.BOMBED;
        assertEquals(Cell.BOMBED, game.getVisibleBoard()[coord.getX()][coord.getY()]);
    }
}
