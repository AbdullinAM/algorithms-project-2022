package sample;

public class MoveResult {

    private final MoveType type;
    private final Checker checker;

    public MoveType getType() {
        return type;
    }

    public Checker getChecker() {
        return checker;
    }

    public MoveResult(MoveType type) {
        this(type, null);
    }

    public MoveResult(MoveType type, Checker checker) {
        this.type = type;
        this.checker = checker;
    }
}