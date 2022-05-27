package sample;

import org.junit.Test;

import static org.junit.Assert.*;

public class CheckerTypeTest {
    @Test
    public void isKingTest() {
        assertTrue(CheckerType.W_KING.isKing());
        assertFalse(CheckerType.WHITE.isKing());
        assertTrue(CheckerType.B_KING.isKing());
        assertFalse(CheckerType.BLACK.isKing());
    }
    @Test
    public void isCommonTest() {
        assertTrue(CheckerType.WHITE.isCommon());
        assertFalse(CheckerType.W_KING.isCommon());
        assertTrue(CheckerType.BLACK.isCommon());
        assertFalse(CheckerType.B_KING.isCommon());
    }
    @Test
    public void isWhiteColorTest() {
        assertTrue(CheckerType.W_KING.isWhiteColor());
        assertTrue(CheckerType.WHITE.isWhiteColor());
        assertFalse(CheckerType.B_KING.isWhiteColor());
        assertFalse(CheckerType.BLACK.isWhiteColor());
    }
    @Test
    public void isBlackColorTest() {
        assertTrue(CheckerType.BLACK.isBlackColor());
        assertTrue(CheckerType.B_KING.isBlackColor());
        assertFalse(CheckerType.WHITE.isBlackColor());
        assertFalse(CheckerType.W_KING.isBlackColor());
    }
}