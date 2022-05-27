package sample;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {

    Cell cellW = new Cell(true, 0, 0);

    @Test
    public void hasChecker() {
        assertFalse(cellW.hasChecker());
        cellW.setChecker(new Checker(CheckerType.WHITE, 0, 0));
        assertTrue(cellW.hasChecker());
    }
    @Test
    public void getChecker() {
        assertNull(cellW.getChecker());
        cellW.setChecker(new Checker(CheckerType.WHITE, 0, 0));
        assertEquals(CheckerType.WHITE, cellW.getChecker().getType());
        assertEquals(0, cellW.getChecker().getX(), 0);
        assertEquals(0, cellW.getChecker().getY(), 0);
    }
}