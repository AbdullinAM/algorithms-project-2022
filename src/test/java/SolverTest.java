import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class SolverTest {
    @Test
    public void solverTest() {
        Game.tiles[0][0] = 5;
        Game.tiles[0][1] = 1;
        Game.tiles[0][2] = 6;
        Game.tiles[0][3] = 2;
        Game.tiles[1][0] = 7;
        Game.tiles[1][1] = 11;
        Game.tiles[1][2] = 9;
        Game.tiles[1][3] = 8;
        Game.tiles[2][0] = 14;
        Game.tiles[2][1] = 12;
        Game.tiles[2][2] = 3;
        Game.tiles[2][3] = 4;
        Game.tiles[3][0] = 0;
        Game.tiles[3][1] = 15;
        Game.tiles[3][2] = 13;
        Game.tiles[3][3] = 10;

        Solver h = new Solver();
        assertEquals(h.helper(), "RRRULLDRUURDDLLLURRDLLURRULLURRRDDLULURRDDD");
    }

    @Test
    public void solverTest2() {
        Game.tiles[0][0] = 3;
        Game.tiles[0][1] = 4;
        Game.tiles[0][2] = 2;
        Game.tiles[0][3] = 10;
        Game.tiles[1][0] = 15;
        Game.tiles[1][1] = 13;
        Game.tiles[1][2] = 5;
        Game.tiles[1][3] = 8;
        Game.tiles[2][0] = 12;
        Game.tiles[2][1] = 14;
        Game.tiles[2][2] = 11;
        Game.tiles[2][3] = 0;
        Game.tiles[3][0] = 6;
        Game.tiles[3][1] = 7;
        Game.tiles[3][2] = 1;
        Game.tiles[3][3] = 9;

        Solver h = new Solver();
        assertEquals(h.helper(), "LDLURDRULDLLUURDDLUURRULLDRRRULLDDRDLLURRRULDRDLLURRD");
    }
}