package sample;

import org.junit.Test;
import static org.junit.Assert.*;

public class MoveResultTest {

    MoveResult m = new MoveResult(MoveType.NORMAL, new Checker(CheckerType.WHITE, 0, 0));
    MoveResult n = new MoveResult(MoveType.NONE);

    @Test
    public void getTypeTest() {
        assertEquals(MoveType.NORMAL, m.getType());
        assertEquals(MoveType.NONE, n.getType());
    }
    @Test
    public void getCheckerTest() {
        assertNull(n.getChecker());
        assertEquals(CheckerType.WHITE, m.getChecker().getType());
        assertEquals(0, m.getChecker().getX(), 0);
        assertEquals(0, m.getChecker().getY(), 0);
    }
}