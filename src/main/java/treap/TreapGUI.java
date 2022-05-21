package treap;

import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class TreapGUI extends Application {
    private static final DoubleProperty windowWidth = new SimpleDoubleProperty(810);
    private static final DoubleProperty windowHeight = new SimpleDoubleProperty(540);
    public static Treap treap;
    public static boolean empty = true;
    public static TreapGUI.type type;
    public static int size = 0;

    public enum type {
        STRING,
        DOUBLE
    }

    public static DoubleProperty getWindowWidth() {
        return windowWidth;
    }

    public static DoubleProperty getWindowHeight() {
        return windowHeight;
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(TreapGUI.class.getResource("/TreapMain.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 810, 540);
        scene.heightProperty().addListener((observableValue, number, t1) -> windowHeight.setValue(t1));
        scene.widthProperty().addListener((observableValue, number, t2) -> windowWidth.setValue(t2));
        stage.setMinWidth(810);
        stage.setMinHeight(540);
        stage.setScene(scene);
        stage.setTitle("Treap viewer");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static DoubleProperty drawCircle(AnchorPane pane, double x, double y, Object dataX, Integer dataY, @Nullable DoubleProperty property) {
        Circle circle = new Circle();
        TextField text = new TextField();
        TextField priority = new TextField();
        priority.setText(String.valueOf(dataY));
        priority.setEditable(false);
        text.setEditable(false);
        text.setText(dataX.toString());
        priority.setMaxHeight(20);
        text.setMaxHeight(20);
        text.setMaxWidth(40);
        priority.setMaxWidth(40);
        ReadOnlyDoubleProperty size_property = getWindowWidth();
        if (size > 5) {
            size_property = pane.prefWidthProperty();
        }
        if (property != null) {
        }
        if (property != null) {
            Line line = new Line();
            line.setEndX(property.doubleValue());
            line.setStartX(property.doubleValue() + size_property.doubleValue() * x);
            circle.setCenterX(property.doubleValue() + size_property.doubleValue() * x);
            text.setLayoutX(property.doubleValue() + size_property.doubleValue() * x - 20);
            priority.setLayoutX(property.doubleValue() + size_property.doubleValue() * x - 20);
            ReadOnlyDoubleProperty finalSize_property = size_property;
            property.addListener((observableValue, number, t1) -> {
                circle.setCenterX(t1.doubleValue() + finalSize_property.doubleValue() * x);
                text.setLayoutX(t1.doubleValue() + finalSize_property.doubleValue() * x - 20);
                priority.setLayoutX(t1.doubleValue() + finalSize_property.doubleValue() * x - 20);
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
            text.setLayoutX(size_property.doubleValue() * 0.5 - 20);
            priority.setLayoutX(size_property.doubleValue() * 0.5 - 20);
            size_property.addListener((observableValue, number, t1) -> {
                circle.setCenterX(t1.doubleValue() * 0.5);
                text.setLayoutX(t1.doubleValue() * 0.5 - 20);
                priority.setLayoutX(t1.doubleValue() * 0.5 - 20);
            });
        }
        text.setLayoutY(y - 25);
        priority.setLayoutY(y + 5);
        circle.setCenterY(y);
        circle.setRadius(40);
        pane.getChildren().add(circle);
        pane.getChildren().add(text);
        pane.getChildren().add(priority);
        return circle.centerXProperty();
    }

    public static void ScanLevels(AnchorPane pane, ArrayList<Pair<Treap.Node, DoubleProperty>> nodes, int level) {
        ArrayList<Pair<Treap.Node, DoubleProperty>> result = new ArrayList<>();
        boolean stop = true;
        double constant = 0.25 / Math.pow(2, level - 1);
        for (Pair<Treap.Node, DoubleProperty> node : nodes) {
            if (node != null) {
                stop = false;
                if (node.getKey().left != null) {
                    DoubleProperty left = drawCircle(pane, -constant, 40 + 100 * level, node.getKey().left.x, node.getKey().left.y, node.getValue());
                    Pair<Treap.Node, DoubleProperty> propertyleft = new Pair<>(node.getKey().left, left);
                    result.add(propertyleft);
                } else result.add(null);
                if (node.getKey().right != null) {
                    DoubleProperty right = drawCircle(pane, constant, 40 + 100 * level, node.getKey().right.x, node.getKey().right.y, node.getValue());
                    Pair<Treap.Node, DoubleProperty> propertyright = new Pair<>(node.getKey().right, right);
                    result.add(propertyright);
                } else result.add(null);
            } else {
                result.add(null);
                result.add(null);
            }
        }
        if (!stop)
            ScanLevels(pane, result, level + 1);
        else
            size = level;
    }

    public static void drawTreap(AnchorPane pane) {
        System.out.println("размер дерева" + size);
        if (size > 5) {
            pane.setPrefSize(85 * Math.pow(2, size - 1), 40 + 100 * (size - 1));
        } else
            pane.setPrefSize(810, 500);
        pane.getChildren().clear();
        if (treap==null) return;
        DoubleProperty x = drawCircle(pane, 0, 40, treap.root.x, treap.root.y, null);
        ArrayList<Pair<Treap.Node, DoubleProperty>> array = new ArrayList<>();
        Pair<Treap.Node, DoubleProperty> pair = new Pair<>(treap.root, x);
        array.add(pair);
        ScanLevels(pane, array, 1);
    }
}
