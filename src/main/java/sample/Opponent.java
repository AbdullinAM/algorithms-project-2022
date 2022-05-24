package sample;

public enum Opponent {
    WHITE, BLACK;

    @Override
    public String toString() {
        return this == WHITE ? "Белые" : "Чёрные";
    }

    public Opponent opposite() {
        return this == WHITE ? BLACK : WHITE;
    }
}
