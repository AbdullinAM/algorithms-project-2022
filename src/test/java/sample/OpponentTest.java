package sample;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OpponentTest {
    @Test
    public void toStringTest() {
        assertEquals("Белые", Opponent.WHITE.toString());
        assertEquals("Чёрные", Opponent.BLACK.toString());
    }
    @Test
    public void oppositeTest() {
        assertEquals(Opponent.WHITE, Opponent.BLACK.opposite());
        assertEquals(Opponent.BLACK, Opponent.WHITE.opposite());
    }
}
