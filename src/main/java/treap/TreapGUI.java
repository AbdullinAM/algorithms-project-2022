package treap;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class TreapGUI extends Application {
    private static final DoubleProperty windowWidth = new SimpleDoubleProperty(810);
    public static Treap treap;
    public static boolean empty = true;
    public static type type;
    public static int size = 0;
    public static AnchorPane pane;
    public enum type {
        STRING,
        DOUBLE
    }

    public static DoubleProperty getWindowWidth() {
        return windowWidth;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TreapGUI.class.getResource("/TreapMain.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 810, 540);
        scene.widthProperty().addListener((observableValue, number, t2) -> windowWidth.setValue(t2));
        stage.setMinWidth(810);
        stage.setMinHeight(540);
        stage.setScene(scene);
        stage.setTitle("Treap viewer");
        stage.getIcons().add(new javafx.scene.image.Image("/peace_symbol_PNG92.png"));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void SetPane(AnchorPane anchorPane) {
        pane=anchorPane;
    }

    public static DoubleProperty drawCircle(double x, double y, Object dataX, Integer dataY, @Nullable DoubleProperty property) {
        Circle circle = new Circle();
        Label text = new Label();
        Label priority = new Label();
        priority.setAlignment(Pos.CENTER);
        text.setAlignment(Pos.CENTER);
        priority.setTextFill(Color.WHITE);
        text.setTextFill(Color.WHITE);
        text.setFont(Font.font(14));
        priority.setFont(Font.font(14));
        circle.setStrokeWidth(2);
        Color color =  Color.web("cc9d1d");
        circle.setStroke(color);
        priority.setText("Y=" + dataY);
        text.setText(dataX.toString());
        priority.setMaxHeight(20);
        text.setMaxHeight(20);
        text.setMaxWidth(50);
        priority.setMaxWidth(50);
        text.setOnMouseEntered(mouseEvent -> {
            if (text.getText().length() > 6) {
                text.setMaxWidth(999999);
                text.setTextFill(Color.BLACK);
                text.setBackground(Background.fill(Color.WHITE));
            }
        });
        text.setOnMouseExited(mouseEvent -> {
            if (text.getText().length() > 6) {
                text.setMaxWidth(50);
                text.setBackground(Background.EMPTY);
                text.setTextFill(Color.WHITE);
            }
        });
        ReadOnlyDoubleProperty size_property = getWindowWidth();
        if (size > 5) {
            size_property = pane.prefWidthProperty();
        }
        if (property != null) {
            Line line = new Line();
            line.setEndX(property.doubleValue());
            line.setStartX(property.doubleValue() + size_property.doubleValue() * x);
            circle.setCenterX(property.doubleValue() + size_property.doubleValue() * x);
            text.setLayoutX(property.doubleValue() + size_property.doubleValue() * x - 25);
            priority.setLayoutX(property.doubleValue() + size_property.doubleValue() * x - 25);
            ReadOnlyDoubleProperty finalSize_property = size_property;
            property.addListener((observableValue, number, t1) -> {
                circle.setCenterX(t1.doubleValue() + finalSize_property.doubleValue() * x);
                text.setLayoutX(t1.doubleValue() + finalSize_property.doubleValue() * x - 25);
                priority.setLayoutX(t1.doubleValue() + finalSize_property.doubleValue() * x - 25);
                line.setEndX(t1.doubleValue());
                line.setStartX(t1.doubleValue() + finalSize_property.doubleValue() * x);
            });
            line.setStrokeWidth(5);
            line.setStartY(y);
            line.setEndY(y - 100);
            pane.getChildren().add(line);
            line.toBack();
        } else {
            circle.setCenterX(size_property.doubleValue() * 0.5);
            text.setLayoutX(size_property.doubleValue() * 0.5 - 25);
            priority.setLayoutX(size_property.doubleValue() * 0.5 - 25);
            size_property.addListener((observableValue, number, t1) -> {
                circle.setCenterX(t1.doubleValue() * 0.5);
                text.setLayoutX(t1.doubleValue() * 0.5 - 25);
                priority.setLayoutX(t1.doubleValue() * 0.5 - 25);
            });
        }
        text.setLayoutY(y - 20);
        priority.setLayoutY(y);
        circle.setCenterY(y);
        circle.setRadius(40);
        pane.getChildren().add(circle);
        pane.getChildren().add(text);
        pane.getChildren().add(priority);
        return circle.centerXProperty();
    }

    public static void ScanLevels(ArrayList<Pair<Treap.Node, DoubleProperty>> nodes, int level) {
        ArrayList<Pair<Treap.Node, DoubleProperty>> result = new ArrayList<>();
        boolean stop = true;
        double constant = 0.25 / Math.pow(2, level - 1);
        DoubleProperty left = null;
        DoubleProperty right = null;
        for (Pair<Treap.Node, DoubleProperty> node : nodes) {
            stop = false;
            if (node.getKey().left != null) {
                left = drawCircle(-constant, 40 + 100 * level, node.getKey().left.x, node.getKey().left.y, node.getValue());
                Pair<Treap.Node, DoubleProperty> propertyleft = new Pair<>(node.getKey().left, left);
                result.add(propertyleft);
            }
            if (right != null && left != null && Math.abs(left.doubleValue() - right.doubleValue()) < 85) {
                System.out.println(Math.abs(left.doubleValue() - right.doubleValue()));
                size = level+2;
                drawTreap();
                return;
            }
            if (node.getKey().right != null) {
                right = drawCircle(constant, 40 + 100 * level, node.getKey().right.x, node.getKey().right.y, node.getValue());
                Pair<Treap.Node, DoubleProperty> propertyright = new Pair<>(node.getKey().right, right);
                result.add(propertyright);
            }
            if (right != null && left != null && Math.abs(right.doubleValue() - left.doubleValue()) < 85) {
                System.out.println(Math.abs(right.doubleValue() - left.doubleValue()));
                size = level+2;
                drawTreap();
                return;
            }
        }
        if (!stop)
            ScanLevels(result, level + 1);
        else {
            System.out.println(size);
            System.out.println(level);
            if (level-2 <= size) {
                size = level-2;
            }
        }
    }

    public static void drawTreap() {
        System.out.println("размер дерева" + size);
        if (size > 3) {
            pane.setPrefSize(85 * Math.pow(2, size-2), 40 + 100 * (size));
        } else
            pane.setPrefSize(810, 500);
        pane.getChildren().clear();
        if (treap == null) return;
        DoubleProperty x = drawCircle(0, 40, treap.root.x, treap.root.y, null);
        ArrayList<Pair<Treap.Node, DoubleProperty>> array = new ArrayList<>();
        Pair<Treap.Node, DoubleProperty> pair = new Pair<>(treap.root, x);
        array.add(pair);
        ScanLevels(array, 1);
    }
}
