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
        if (n == TreapGUI.treap.root && n.left == null && n.right == null) {
            TreapGUI.empty = true;
            TreapGUI.treap = null;
        } else if (n == TreapGUI.treap.root){
            System.out.println("RootOld="+TreapGUI.treap.root.x);
            System.out.println(toFind);
            TreapGUI.treap.remove(TreapGUI.treap.root, (Comparable) toFind);
            System.out.println("RootNew="+TreapGUI.treap.root.x);
        } else
            TreapGUI.treap.remove(TreapGUI.treap.root, (Comparable) toFind);
        TreapGUI.drawTreap(mainpane);
    }

    @FXML
    private void add() {
        String text = data.getText();
        if (text.isEmpty()) return;
        if (TreapGUI.empty) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(stage);
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
        TreapGUI.drawTreap(mainpane);
    }

    @FXML
    private void clear() {
        TreapGUI.empty = true;
        TreapGUI.treap = null;
        TreapGUI.drawTreap(mainpane);
    }
}
