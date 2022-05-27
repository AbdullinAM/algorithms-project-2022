package sample;

import org.junit.Test;

import static org.junit.Assert.*;
import static sample.CheckersApp.CELL_SIZE;

public class CheckerTest {

    Checker c = new Checker(CheckerType.WHITE, 0, 0);

    @Test
    public void getType(){
        assertEquals(CheckerType.WHITE, c.getType());
    }
    @Test
    public void getX(){
        assertEquals(0, c.getX(), 0);
    }
    @Test
    public void getY(){
        assertEquals(0, c.getY(), 0);
    }
    @Test
    public void toKingTypeTest() {
        c.toKingType();
        assertEquals(CheckerType.W_KING, c.getType());
    }
    @Test
    public void moveTest() {
        c.move(1,1);
        assertEquals(CELL_SIZE, c.getX(), 0);
        assertEquals(CELL_SIZE, c.getY(), 0);
    }
}
