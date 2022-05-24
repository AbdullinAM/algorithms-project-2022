package sample;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import static sample.CheckersApp.CELL_SIZE;


public class Checker extends StackPane {

    private CheckerType type;
    private final Circle circle;

    private double mouseX, mouseY;
    private double x, y;

    public CheckerType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Checker(CheckerType type, int x, int y) {
        this.type = type;
        move(x, y);

        this.circle = new Circle(CELL_SIZE * 0.35);
        circle.setFill(type.isBlackColor() ? Color.BLACK : Color.WHITE);
        circle.setStroke(switch (type) {
            case WHITE -> Color.LIGHTGREY;
            case BLACK -> Color.valueOf("#484848");
            default -> Color.LIGHTGOLDENRODYELLOW;
        });
        circle.setStrokeWidth(CELL_SIZE * (type.isKing() ?  0.1 : 0.03));
        circle.setTranslateX(CELL_SIZE / (type.isKing() ?  9.5 :  7.0));
        circle.setTranslateY(CELL_SIZE / (type.isKing() ?  9.5 :  7.0));
        getChildren().add(circle);

        setOnMousePressed(event -> {
            mouseX = event.getSceneX();
            mouseY = event.getSceneY();
        });


        setOnMouseDragged(event -> {
            this.toFront();
            relocate(event.getSceneX() - mouseX + this.x, event.getSceneY() - mouseY + this.y);
        });
    }

    public void toKingType() {
        this.type = (this.type == CheckerType.BLACK) ? CheckerType.B_KING : CheckerType.W_KING;
        circle.setStroke(Color.LIGHTGOLDENRODYELLOW);
        circle.setStrokeWidth(CELL_SIZE * 0.1);
        circle.setTranslateX(CELL_SIZE / 9.5);
        circle.setTranslateY(CELL_SIZE / 9.5);

    }

    public void move(int x, int y) {
        this.x = x * CELL_SIZE;
        this.y = y * CELL_SIZE;
        relocate(this.x, this.y);
    }

    public void abortMove() {
        relocate(x, y);
    }
}