package treap;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Optional;

public class TreapController {
    @FXML
    private TextField data;
    @FXML
    private AnchorPane mainpane;
    private Stage stage;

    @FXML
    private void delete() {
        String text = data.getText();
        if (text.isEmpty() || TreapGUI.empty) return;
        TreapGUI.type type = TreapGUI.type;
        Object toFind;
        if (type == TreapGUI.type.DOUBLE) {
            toFind = Double.parseDouble(text);
        } else {
            toFind = text;
        }
        Treap.Node n = TreapGUI.treap.find((Comparable) toFind);
        if (n == null) {
            ImageView failView = new ImageView("/11.png");
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Элемент не найден");
            alert.setHeaderText("Удаляемого элемента не существует");
            alert.setGraphic(failView);
            Optional<ButtonType> result = alert.showAndWait();
        } else if (n == TreapGUI.treap.root && n.left == null && n.right == null) {
            TreapGUI.empty = true;
            TreapGUI.treap = null;
        } else
            TreapGUI.treap.remove(TreapGUI.treap.root, (Comparable) toFind);
        TreapGUI.drawTreap();
    }

    @FXML
    private void add() {
        String text = data.getText();
        if (text.isEmpty()) return;
        if (TreapGUI.empty) {
            TreapGUI.SetPane(mainpane);
            ImageView failView = new ImageView("/35.png");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(stage);
            alert.setGraphic(failView);
            alert.setTitle("Выберите тип данных, хранимых в Treap.");
            alert.setHeaderText("Его нельзя будет изменить до удаления всех узлов!");
            ButtonType string = new ButtonType("Строки");
            ButtonType doubles = new ButtonType("Числа");
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(string, doubles);
            Optional<ButtonType> option = alert.showAndWait();
            if (option.get() == string) {
                TreapGUI.treap = new Treap<String>();
                TreapGUI.type = TreapGUI.type.STRING;
            } else {
                TreapGUI.treap = new Treap<Double>();
                TreapGUI.type = TreapGUI.type.DOUBLE;
            }
            TreapGUI.empty = false;
        }
        TreapGUI.type type = TreapGUI.type;
        if (type == TreapGUI.type.DOUBLE) {
            TreapGUI.treap.add(Double.parseDouble(text));
        } else {
            TreapGUI.treap.add(text);
        }
        TreapGUI.drawTreap();
    }

    @FXML
    private void clear() {
        ImageView clearView = new ImageView("/18.png");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Продолжить?");
        alert.setHeaderText("Вы собираетесь очистить дерево!");
        alert.setGraphic(clearView);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            TreapGUI.empty = true;
            TreapGUI.treap = null;
            TreapGUI.drawTreap();
        } else {
            alert.close();
        }
    }
}
