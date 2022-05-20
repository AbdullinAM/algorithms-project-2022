package loading;

import controllers.MainWindowController;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import logic.fifteen.State;
import logic.solver.Solver;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static final ArrayList<Integer> listOfNumbers = generateNumbers();

    public void start(Stage primaryStage) throws IOException {
        initButtons(listOfNumbers);
        FXMLLoader loader = new FXMLLoader();
        URL url = getClass().getResource("/res/MainWindow.fxml");
        loader.setLocation(url);
        AnchorPane root = loader.load();
        GridPane buttonsGrid = MainWindowController.buttonsGrid;
        buttonsGrid.setLayoutX(20);
        buttonsGrid.setLayoutY(20);
        root.getChildren().add(buttonsGrid);
        Scene mainScene = new Scene(root);
        primaryStage.setScene(mainScene);
        primaryStage.setTitle("Игра в 15!");
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static ArrayList<Integer> generateNumbers() {
        ArrayList<Integer> res = new ArrayList<>();
        int zeroId = 0;
        for (int i = 0; i < 16; i++) {
            Random random = new Random();
            int r = random.nextInt(16);
            if (r == 0) zeroId = i;
            if (!res.contains(r)) {
                res.add(r);
            } else {
                i--;
            }
        }
        State s = new State(Solver.convertToArray(res), null);
        if (!Solver.isSolvable(s)) {
            if (zeroId > 11) {
                Collections.swap(res, zeroId, zeroId - 4);
            } else {
                Collections.swap(res, zeroId, zeroId + 4);
            }
        }
        return res;
    }

    public static void initButtons(ArrayList<Integer> listOfNumbers) {
        GridPane gridOfButtons = MainWindowController.buttonsGrid;
        gridOfButtons.setAlignment(Pos.CENTER);
        for (int i = 0; i < 16; i++) {
            Button button = new Button();
            button.setMaxHeight(60);
            button.setMinHeight(60);
            button.setMinWidth(60);
            button.setMaxWidth(60);
            if (listOfNumbers.get(i) != 0) {
                button.setText(String.valueOf(listOfNumbers.get(i)));
            }
            gridOfButtons.add(button, i / 4, i % 4);
        }

    }
}