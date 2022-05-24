package sample;

public enum CheckerType {
    BLACK, WHITE, W_KING, B_KING;

    public boolean isKing() {
        return  this == B_KING || this == W_KING;
    }

    public boolean isCommon() {
        return  this == WHITE || this == BLACK;
    }

    public boolean isWhiteColor() {
        return  this == WHITE || this == W_KING;
    }

    public boolean isBlackColor() {
        return  this == BLACK || this == B_KING;
    }
}
