package treap;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Unmarshaller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import javax.xml.bind.JAXBException;
import java.io.File;
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
            alert.showAndWait();
        } else if (n == TreapGUI.treap.root && n.left == null && n.right == null) {
            TreapGUI.empty = true;
            TreapGUI.treap = null;
        } else
            TreapGUI.treap.remove(TreapGUI.treap.root, (Comparable) toFind);
        TreapGUI.drawTreap();
    }

    @FXML
    private void load() throws jakarta.xml.bind.JAXBException, ClassNotFoundException {
        JAXBContext context = JAXBContext.newInstance(Treap.class);
        Unmarshaller um = context.createUnmarshaller();
        File file = new File("src/main/resources/save.xml");
        TreapGUI.treap = (Treap) um.unmarshal(file);
        if (TreapGUI.treap.root.x == null)
            return;
        TreapGUI.empty = false;
        TreapGUI.SetPane(mainpane);
        if (TreapGUI.treap.root.x.getClass() == Class.forName("java.lang.Double"))
            TreapGUI.type = TreapGUI.type.DOUBLE;
        else
            TreapGUI.type = TreapGUI.type.STRING;
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
            try {
                TreapGUI.treap.add(Double.parseDouble(text));
            } catch (NumberFormatException e) {
                ImageView failView = new ImageView("/11.png");
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Некорректный элемент");
                alert.setHeaderText("Видимо, вы попытались загрузить строку в дерево чисел");
                alert.setGraphic(failView);
                alert.showAndWait();
            }
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
