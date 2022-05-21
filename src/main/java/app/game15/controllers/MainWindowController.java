package app.game15.controllers;

import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import app.game15.loading.MainWindow;
import app.game15.logic.fifteen.State;
import app.game15.logic.solver.Solver;

import java.util.ArrayList;

public class MainWindowController {

    public static GridPane buttonsGrid = new GridPane();

    public Button generate;
    public Button solve;

    public void startSolution() {
        State state = new State(Solver.convertToArray(MainWindow.listOfNumbers), null);
        Solver solver = new Solver(state);
        solver.solve();
    }

    public void startGeneration() {
        ArrayList<Integer> listOfNumbers = MainWindow.generateNumbers();
        MainWindow.initButtons(listOfNumbers);
    }
}
