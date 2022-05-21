import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.Random;
import static org.junit.jupiter.api.Assertions.*;

public class MineSweeperTest {
    Random r = new Random();

    @Test
    public void solve() {
        MineSweeper ms = new MineSweeper(10, 10);
        Game game = ms.getGame();
        assertEquals(game.getState(), GameState.PLAYING);

        Cell[][] expected = game.getVisibleBoard();
        ms.startNewGame();
        game = ms.getGame();
        assertNotEquals(expected, game.getVisibleBoard());

        //expected = game.getVisibleBoard();
        ms.startSolve();

        expected = game.getVisibleBoard();
        int k = 0;
        int empty = 0;
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                if (expected[x][y] == Cell.CLOSED && game.getHiddenBoard()[x][y] == Cell.BOMB)
                    k++;
                else if (expected[x][y] == Cell.CLOSED)
                    empty++;
            }
        }
        if (game.getState() == GameState.WIN) {
            assertEquals(k, game.getBombsAmount());
        } else if (game.getState() == GameState.PLAYING) {
            int emp = 0;
            for (int x = 0; x < 10; x++) {
                for (int y = 0; y < 10; y++) {
                    if (expected[x][y] == Cell.CLOSED)
                        emp++;
                }
            }
            assertEquals(k, game.getBombsAmount());
            assertEquals(emp, game.getBombsAmount() + empty);
        }
    }
}