package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;

public class CheckersApp extends Application {

    public static final int CELL_SIZE = 90;
    //public static final int UNREAL_DIR_OR_INDEX = -2;

    private Pair<Integer,Integer> computerLastMove = null;

    public ComputerSide boardToState(){
        int[][] state = new int[8][8];
        for (int y = 0; y < 8; y++)
            for (int x = 0; x < 8; x++) {
                if (!board[x][y].hasChecker()) continue;
                switch (board[x][y].getChecker().getType()) {
                    case BLACK -> state[y][x] = 1;
                    case WHITE -> state[y][x] = -1;
                    case B_KING -> state[y][x] = 3;
                    default -> state[y][x] = -3;
                }
            }
        int computerSide;
        if (computerPlayer == Opponent.BLACK) computerSide = 1;
        else computerSide = -1;
        return new ComputerSide(state, computerSide, computerLastMove);
    }

    private final Cell[][] board = new Cell[8][8];
    private final Group cellsGroup = new Group();
    private final Group checkersGroup = new Group();
    private final Label state = new Label();
    private final Label err = new Label();
    private final Button restart = new Button();
    {
        restart.setFocusTraversable(false);
        restart.setMinHeight(CELL_SIZE / 2.0);
        restart.setMinWidth(CELL_SIZE * 2);
        restart.setText("restart?");
        restart.setFont(new Font(20));
        restart.relocate(CELL_SIZE * 5.75, CELL_SIZE * 8.25);
        restart.setOnMouseClicked(event -> {
            next = null;
            player = Opponent.WHITE;
            numWhiteCheckers = 12;
            numBlackCheckers = 12;
            createChoosingWindow();
            state.setText("Начало игры: ходят белые");
            err.setText("");
            checkersGroup.getChildren().removeAll(checkersGroup.getChildren());
            for (int y = 0; y < 8; y++)
                for (int x = 0; x < 8; x++) {
                    Checker checker = null;
                    if ((x + y) % 2 != 0) {
                        if (y <= 2) checker = makeChecker(CheckerType.BLACK, x, y);
                        else if (y >= 5) checker = makeChecker(CheckerType.WHITE, x, y);
                    }
                    board[x][y].setChecker(checker);
                    if (checker != null) checkersGroup.getChildren().add(checker);
                }
        });
    }

    Checker next;
    Opponent player;
    Opponent computerPlayer;
    int numWhiteCheckers;
    int numBlackCheckers;

    public void setPlayer() {
        player = player.opposite();
    }

    public void decreaseNumChecker(CheckerType type, int n) {
        if (type.isBlackColor()) numBlackCheckers -= n;
        else numWhiteCheckers -= n;
    }

    public Opponent winner() {
        if (numWhiteCheckers == 0)
            return Opponent.BLACK;
        else if (numBlackCheckers == 0)
            return Opponent.WHITE;
        return null;
    }

    @Override
    public void start(Stage primaryStage) {

        Parent p = createBoard();

        Scene scene = new Scene(p);
        primaryStage.setTitle("CheckersApp");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
        createChoosingWindow();
        if (computerPlayer == Opponent.WHITE){
            ComputerSide t = boardToState();
            int[] i = t.minimaxStart(2);
            computerLastMove = new Pair<>(i[3],i[2]);
            move(board[i[0]][i[1]].getChecker(),i[0],i[1],i[2],i[3]);
        }
    }

    public void createChoosingWindow(){
        Stage choosingWindow = new Stage();

        choosingWindow.initModality(Modality.APPLICATION_MODAL);
        choosingWindow.setResizable(false);
        choosingWindow.initStyle(StageStyle.UNDECORATED);
        VBox p = new VBox();

        Button white = new Button("white");
        white.setOnAction(event->{
            computerPlayer = Opponent.BLACK;
            choosingWindow.close();
        });
        Button black = new Button("black");
        black.setOnAction(event->{
            computerPlayer = Opponent.WHITE;
            choosingWindow.close();
        });

        p.setAlignment(Pos.CENTER);

        p.getChildren().add(white);
        p.getChildren().add(black);

        white.setFocusTraversable(false);
        black.setFocusTraversable(false);

        white.setMaxWidth(Double.MAX_VALUE);
        black.setMaxWidth(Double.MAX_VALUE);
        white.setMinHeight(CELL_SIZE);
        black.setMinHeight(CELL_SIZE);

        Scene scene = new Scene(p, 3*CELL_SIZE, 2*CELL_SIZE);
        choosingWindow.setScene(scene);
        choosingWindow.setTitle("Выберите цвет");

        choosingWindow.showAndWait();
    }

    public Parent createBoard() {
        next = null;
        player = Opponent.WHITE;
        numWhiteCheckers = 12;
        numBlackCheckers = 12;
        Pane root = new Pane();
        root.setPrefSize(8 * CELL_SIZE, 9 * CELL_SIZE);
        root.getChildren().addAll(cellsGroup, checkersGroup);
        root.getChildren().addAll(state, err);
        root.getChildren().add(restart);

        state.setText("Начало игры: ходят белые");
        state.setFont(new Font(20));
        state.relocate(0, CELL_SIZE * 8.25);

        err.setText("");
        err.setFont(new Font(14));
        err.relocate(0, CELL_SIZE * 8.5);

        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                Cell cell = new Cell((x + y) % 2 == 0, x, y);
                board[x][y] = cell;
                cellsGroup.getChildren().add(cell);
                Checker checker = null;
                if ((x + y) % 2 != 0) {
                    if (y <= 2) checker = makeChecker(CheckerType.BLACK, x, y);
                    else if (y >= 5) checker = makeChecker(CheckerType.WHITE, x, y);
                    if (checker != null) {
                        cell.setChecker(checker);
                        checkersGroup.getChildren().add(checker);
                    }
                }
            }
        }
        return root;
    }

    public void move(Checker checker, int x0, int y0, int newX, int newY){
        List<MoveResult> result = tryMove(checker, newX, newY);


        switch (result.get(0).getType()) {
            case NONE -> checker.abortMove();
            case NORMAL -> {
                checker.move(newX, newY);
                board[x0][y0].setChecker(null);
                board[newX][newY].setChecker(checker);
            }
            case KILL -> {
                checker.move(newX, newY);
                board[x0][y0].setChecker(null);
                board[newX][newY].setChecker(checker);
                decreaseNumChecker(result.get(0).getChecker().getType(), result.size());
                for (MoveResult m : result) {
                    Checker otherChecker = m.getChecker();
                    board[toBoardIndex(otherChecker.getX())][toBoardIndex(otherChecker.getY())].setChecker(null);
                    checkersGroup.getChildren().remove(otherChecker);
                }
                if (winner() != null) {
                    state.setText(winner().toString() + " победили");
                }
            }
        }
    }

    public Checker makeChecker(CheckerType type, int x, int y) {//возможно стоит перенести в класс
        Checker checker = new Checker(type, x, y);

        checker.setOnMouseReleased(event -> {//основная часть подойдет и для хода компьютера, нужно вынести в отдельную функцию
            if (computerPlayer == player) {
                checker.abortMove();
                err.setText("Ход шашкой другого игрока");
                return;
            }

            move(checker,toBoardIndex(checker.getX()), toBoardIndex(checker.getY()),
                    toBoardIndex(checker.getLayoutX()), toBoardIndex(checker.getLayoutY()));


            while (computerPlayer == player) {
                ComputerSide t = boardToState();
                int[] i = t.minimaxStart(2);
                computerLastMove = new Pair<>(i[3],i[2]);
                move(board[i[0]][i[1]].getChecker(),i[0],i[1],i[2],i[3]);

            }
        });
        return checker;
    }

    public int toBoardIndex(double pixel) {
        return (int) (pixel + CELL_SIZE / 2) / CELL_SIZE;
    }

    public boolean sameColor(int x, int y, CheckerType type) {
        return board[x][y].hasChecker() && (board[x][y].getChecker().getType().isBlackColor() && type.isBlackColor()
                || board[x][y].getChecker().getType().isWhiteColor() && type.isWhiteColor());
    }

    public List<MoveResult> tryMove(Checker checker, int newX, int newY) {
        List<MoveResult> none = new ArrayList<>();
        none.add(new MoveResult(MoveType.NONE));
        CheckerType type = checker.getType();
        int x0 = toBoardIndex(checker.getX());
        int y0 = toBoardIndex(checker.getY());
        int delta = Math.abs(newX - x0);

        if (winner() != null) {
            err.setText("Игра окончена");
            return none;
        }
        if (type.isWhiteColor() && player == Opponent.BLACK || (type.isBlackColor() && player == Opponent.WHITE)) {
            err.setText("Ход шашкой другого игрока");
            return none;
        }
        if (newX < 0 || newY < 0 || newX >= 8 || newY >= 8) {
            err.setText("Выход за границы доски");
            return none;
        }
        if (board[newX][newY].hasChecker()) {
            err.setText("Ход на другую шашку");
            return none;
        }
        if ((newX + newY) % 2 == 0) {
            err.setText("Ход на недопустимую позицию");
            return none;
        }
        if (Math.abs(delta) != Math.abs(newY - y0) || type.isCommon() && delta > 2) {
            err.setText("Недопустимая схема хода");
            return none;
        }

        List<MoveResult> result = new ArrayList<>();
        if (type.isCommon()) {
            if (delta == 1) {
                if (next == null && newY - y0 == (type.isWhiteColor() ? -1 : 1)) {
                    if (fightIsPossibleForColor(type)) {
                        err.setText("Игнорирование возможности взятия");
                        return none;
                    }
                    err.setText("");
                    setPlayer();
                    result.add(new MoveResult(MoveType.NORMAL));
                } else if (next != null) {
                    err.setText("Необходимо продолжить бой");
                    return none;
                } else {
                    err.setText("Недопустимая схема хода");
                    return none;
                }
            } else if (delta == 2) {
                int x1 = (newX + x0) / 2;
                int y1 = (newY + y0) / 2;
                if (next != null && (next.getX() != checker.getX() || next.getY() != checker.getY())) {
                    err.setText("Необходимо продолжить бой");
                    return none;
                }
                if (board[x1][y1].hasChecker()) {
                    if (board[x1][y1].getChecker().getType() != type) {
                        err.setText("");
                        next = null;
                        result.add(new MoveResult(MoveType.KILL, board[x1][y1].getChecker()));

                        if (fightIsPossibleForPosition(type, newX, newY)) {
                            next = new Checker(type, newX, newY);
                        } else {
                            setPlayer();
                        }
                    } else {
                        err.setText("Попытка взятия собственной фишки");
                        return none;
                    }
                } else {
                    err.setText("Недопустимая схема хода");
                    return none;
                }
            } else {
                err.setText("Недопустимая схема хода");
                return none;
            }
            if (type.isWhiteColor() && newY == 0 || type.isBlackColor() && newY == 7) {
                checker.toKingType();
            }
            state.setText("Ход: " + player.toString() + (next != null ? " продолжают бой" : ""));
        } else {
            int signX = (newX - x0) / Math.abs(newX - x0);//направление перемещения (-1 или 1 по обеим осям)
            int signY = (newY - y0) / Math.abs(newY - y0);//

            if (next != null && (next.getX() != checker.getX() || next.getY() != checker.getY())) {
                err.setText("Необходимо продолжить бой");
                return none;
            }

            for (int i = 1; i <= delta; i++) {
                if (board[x0 + i * signX][y0 + i * signY].hasChecker()) {
                    if (sameColor(x0 + i * signX, y0 + i * signY, type)) {
                        err.setText("Попытка взятия собственной фишки");
                        return none;
                    } else {
                        result.add(new MoveResult(MoveType.KILL, board[x0 + i * signX][y0 + i * signY].getChecker()));
                    }
                }
            }
            if (!result.isEmpty()) {
                next = null;
                if (fightIsPossibleForPosition(type, newX, newY))
                    next = new Checker(type, newX, newY);
                else setPlayer();
                state.setText("Ход: " + player.toString() + (next != null ? " продолжают бой" : ""));
                err.setText("");
                return result;
            }
            if (fightIsPossibleForColor(type)) {//TODO вынести в отдельную переменную
                err.setText("Игнорирование возможности взятия");
                return none;
            }
            err.setText("");
            setPlayer();
            state.setText("Ход: " + player.toString());
            result.add(new MoveResult(MoveType.NORMAL));
        }
        return result;
    }

    public boolean fightIsPossibleForPosition(CheckerType type, int x, int y) {
        if (type.isCommon()) {
            return x < 6 && y < 6 && board[x + 1][y + 1].hasChecker() &&
                    !sameColor(x + 1, y + 1, type) && !board[x + 2][y + 2].hasChecker() ||

                    x > 1 && y > 1 && board[x - 1][y - 1].hasChecker() &&
                            !sameColor(x - 1, y - 1, type) && !board[x - 2][y - 2].hasChecker() ||

                    x < 6 && y > 1 && board[x + 1][y - 1].hasChecker() &&
                            !sameColor(x + 1, y - 1, type) && !board[x + 2][y - 2].hasChecker() ||

                    x > 1 && y < 6 && board[x - 1][y + 1].hasChecker() &&
                            !sameColor(x - 1, y + 1, type) && !board[x - 2][y + 2].hasChecker();
        } else {
            boolean killer = false;
            for (int i = -1; i < 2; i += 2)
                for (int j = -1; j < 2; j += 2) {
                    for (int n = 1; x + n * i < 8 && y + n * j < 8 && x + n * i >= 0 && y + n * j >= 0; n++) {
                        if (board[x + n * i][y + n * j].hasChecker()) {
                            if (sameColor(x + n * i, y + n * j, type)) break;
                            killer = true;
                        } else if (killer) return true;
                    }
                    killer = false;
                }
        }
        return false;
    }

    public boolean fightIsPossibleForColor(CheckerType type) {
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 8; x++) {
                if (!board[x][y].hasChecker() || !sameColor(x, y, type)) continue;
                if (fightIsPossibleForPosition(board[x][y].getChecker().getType(), x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void main(String[] args) {
        launch(args);
    }
}