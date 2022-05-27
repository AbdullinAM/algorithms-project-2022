package sample;

import javafx.util.Pair;
import org.junit.Test;

import static org.junit.Assert.*;

public class ComputerTest {

    Computer test = new Computer();

    int[][][] testStates = new int[11][8][8];
    {
        testStates[0] = new int[][]{{0, 1, 0, 1, 0, 1, 0, 1},
                                    {1, 0, 1, 0, 1, 0, 1, 0},
                                    {0, 1, 0, 1, 0, 1, 0, 1},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {-1, 0, -1, 0, -1, 0, -1, 0},
                                    {0, -1, 0, -1, 0, -1, 0, -1},
                                    {-1, 0, -1, 0, -1, 0, -1, 0}};
        testStates[1] = new int[][]{{0, 1, 0, 1, 0, 1, 0, 1},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {-1, 0, -5, 0, -1, 0, -1, 0}};

        testStates[2] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, -1, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        testStates[3] = new int[][]{{0, 0, 0, 0, 0, 1, 0, 0},
                                    {0, 0, 0, 0, 1, 0, 0, 0},
                                    {0, 0, 0, -1, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0}};

        testStates[4] = new int[][]{{0, 0, 0, 0, 0, 1, 0, 0},
                                    {0, 0, 0, 0, -1, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0},
                                    {0, 0, 0, 0, 0, 0, 0, 0}};

        testStates[5] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {-1, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        testStates[6] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {-1, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};

        testStates[7] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 5, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -5, 0, 0, 0, 0, 0}};

        testStates[8] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, -1, 0},
                {0, 0, 0, 0, 0, 5, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -5, 0, 0, 0, 0, 0}};

        testStates[9] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 5, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, -5, 0, 0, 0, 0, 0}};

        testStates[10] = new int[][]{{0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 1},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 5, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, -5, 0, 0, 0, 0, 0}};

    }

    @Test
    public void isFriendTest() {
        assertTrue(test.isFriend(-1));
        assertFalse(test.isFriend(0));
        assertFalse(test.isFriend(1));
    }

    @Test
    public void isEnemyTest() {
        assertTrue(test.isEnemy(1));
        assertFalse(test.isEnemy(0));
        assertFalse(test.isEnemy(-1));
    }

    @Test
    public void isComputerSideTest() {
        assertTrue(test.isComputerSide(-1));
        assertFalse(test.isComputerSide(0));
        assertFalse(test.isComputerSide(1));
    }

    @Test
    public void outOfLimitsTest() {
        assertTrue(Computer.outOfLimits(-1, 0));
        assertTrue(Computer.outOfLimits(0, -1));
        assertTrue(Computer.outOfLimits(8, 0));
        assertTrue(Computer.outOfLimits(0, 8));
        assertFalse(Computer.outOfLimits(0, 7));
    }

    @Test
    public void costTest() {
        assertEquals(0, new Computer(testStates[0], -1, null).cost());
        assertEquals(4, new Computer(testStates[1], -1, null).cost());
    }

    @Test
    public void fightIsPossibleForPositionTest() {
        assertFalse(new Computer(testStates[0], -1, null).fightIsPossibleForPosition(1,0));
        assertFalse(new Computer(testStates[1], -1, null).fightIsPossibleForPosition(1,0));
        assertFalse(new Computer(testStates[2], -1, null).fightIsPossibleForPosition(2,3));
        assertFalse(new Computer(testStates[3], -1, null).fightIsPossibleForPosition(3,2));
        assertFalse(new Computer(testStates[4], -1, null).fightIsPossibleForPosition(4,1));
        assertTrue(new Computer(testStates[5], -1, null).fightIsPossibleForPosition(0,2));
        assertTrue(new Computer(testStates[6], -1, null).fightIsPossibleForPosition(0,2));
        assertTrue(new Computer(testStates[7], -1, null).fightIsPossibleForPosition(2,7));
        assertFalse(new Computer(testStates[8], -1, null).fightIsPossibleForPosition(2,7));
        assertFalse(new Computer(testStates[9], -1, null).fightIsPossibleForPosition(2,7));
        assertTrue(new Computer(testStates[10], -1, null).fightIsPossibleForPosition(2,7));
    }

}
