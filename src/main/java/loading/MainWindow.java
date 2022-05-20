package loading;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.*;
import logic.solver.Solver;
import logic.fifteen.State;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Random;

public class MainWindow extends Application {

    private static final Button[] buttons = new Button[16];
    private static ArrayList<Integer> generatedNumbers = GenerateNumbers();
    private int zeroX = 0;
    private int zeroY = 0;
    private static int zeroId = 0;
    private static final int buttonSize = 50;

    private static int[][] convertToArray(ArrayList<Integer> list) {
        int[][] result = new int[4][4];
        for (int i = 0; i < list.size(); i++) {
            result[i / 4][i % 4] = list.get(i);
        }
        return result;
    }

    private Boolean isSolved() {
        boolean res = true;
        for (int i = 0; i < 15; i++) {
            if (generatedNumbers.get(i) != i) {
                res = false;
                break;
            }
        }
        return res;
    }

    private static ArrayList<Integer> GenerateNumbers() {
        ArrayList<Integer> res = new ArrayList<>();
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
        State s = new State(convertToArray(res), null);
        if (Solver.isSolvable(s)) {
            if (zeroId > 11) {
                Collections.swap(res, zeroId, zeroId - 4);
            } else {
                Collections.swap(res, zeroId, zeroId + 4);
            }
        }
        return res;
    }

    @Override
    public void start(Stage primaryStage) {
        initBtnsArray();
        Group group = new Group();
        group.getChildren().add(getGrid());
        Button buttonStart = new Button("Сгенерировать");
        Button buttonSolve = new Button("Решить");
        buttonStart.setLayoutX(80);
        buttonStart.setLayoutY(210);
        buttonSolve.setLayoutX(10);
        buttonSolve.setLayoutY(210);

        buttonStart.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                generatedNumbers = GenerateNumbers();
                for (int i = 0; i < buttons.length; i++) {
                    buttons[i].setText((generatedNumbers.get(i) + 1) + "");
                    if (generatedNumbers.get(i) == 15) {
                        buttons[i].setText("");
                        zeroX = i % 4;
                        zeroY = i / 4;
                        zeroId = i;
                    }
                }
            }
        });

        buttonSolve.setOnAction(act -> {
            Stage st = new Stage();
            Pane root = new Pane();
            Label l = new Label();
            l.setWrapText(true);
            root.getChildren().add(l);
            Scene sc = new Scene(root);
            st.setHeight(300);
            st.setWidth(300);
            st.setScene(sc);
            st.show();
            startSolution(l);
        });

        group.getChildren().add(buttonStart);
        group.getChildren().add(buttonSolve);
        Scene scene = new Scene(group);

        primaryStage.setTitle("Игра в 15");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void startSolution(Label label) {
        int[][] blocks = convertToArray(generatedNumbers);
        logic.fifteen.State initial = new logic.fifteen.State(blocks, null);

        if (!Solver.isSolvable(initial)) {
            label.setText("Данная комбинация не решаема!");
            return;
        }
        Task task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                Solver solver = new Solver(initial);
                for (logic.fifteen.State board : solver.solution()) {
                    if (isCancelled()) {
                        break;
                    }
                    Thread.sleep(3000);
                    this.updateMessage(board.toString());
                }
                return null;
            }
        };

        label.textProperty().bind(task.messageProperty());
        new Thread(task).start();
    }

    private static Pane getGrid() {
        int i = 0;
        GridPane gridPane = new GridPane();
        for (Button b : buttons) {
            int x = i % 4;
            int y = i / 4;
            gridPane.add(b, x * buttonSize, y * buttonSize);
            i++;
        }
        return gridPane;
    }

    private void initBtnsArray() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new Button((generatedNumbers.get(i) + 1) + "");
            buttons[i].setMaxWidth(buttonSize);
            buttons[i].setMaxHeight(buttonSize);
            buttons[i].setMinWidth(buttonSize);
            buttons[i].setMinHeight(buttonSize);
            if (generatedNumbers.get(i) == 15) {
                buttons[i].setText("");
                zeroX = i % 4;
                zeroY = i / 4;
                zeroId = i;
            }
            buttons[i].setId(i + "");

            buttons[i].setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    Button b = (Button) (event.getSource());
                    int id = Integer.parseInt(b.getId());
                    int x = id % 4;
                    int y = id / 4;

                    if (Math.abs(zeroX - x) + Math.abs(zeroY - y) == 1) {
                        buttons[id].setText("");
                        buttons[zeroId].setText((generatedNumbers.get(id) + 1) + "");
                        int tmp = generatedNumbers.get(zeroId);
                        generatedNumbers.set(zeroId, generatedNumbers.get(id));
                        generatedNumbers.set(id, tmp);
                        zeroX = x;
                        zeroY = y;
                        zeroId = id;
                    }

                    if (isSolved()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Поздравляем!");
                        alert.setHeaderText(null);
                        alert.setContentText("Вы прошли игру");
                        alert.showAndWait();
                    }
                }
            });
        }
    }

    public static void main(String[] args) {
        launch(args);
        ArrayList<Integer> ll2 = new ArrayList<>(Arrays.asList(
                1, 2, 3, 0,
                5, 6, 7, 8,
                9, 10, 11, 12,
                13, 14, 15, 4
        ));
        int[][] blocks = convertToArray(ll2);
        logic.fifteen.State initial = new logic.fifteen.State(blocks, null);
        Solver solver = new Solver(initial);
        for (logic.fifteen.State board : solver.solution()) {
            System.out.println(board + "\n");
        }
    }
}