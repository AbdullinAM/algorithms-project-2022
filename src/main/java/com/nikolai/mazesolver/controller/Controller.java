package com.nikolai.mazesolver.controller;

import com.nikolai.mazesolver.model.Cell;
import com.nikolai.mazesolver.model.MazeSolver;
import com.nikolai.mazesolver.view.ImgCreator;
import com.nikolai.mazesolver.model.Maze;
import com.nikolai.mazesolver.view.WarnAlert;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.TextField;

import java.awt.image.BufferedImage;
import java.util.Deque;

public class Controller {
    public TextField width;
    public TextField height;
    @FXML
    private ImageView img;

    private Maze maze;
    private boolean chekClick = false;


    @FXML
    protected void solveMaze() {
        if (chekClick) {
            MazeSolver mazeSolver = new MazeSolver();
            Deque<Cell> path = mazeSolver.solve(maze);
            maze.addPath(path);
            ImgCreator imgCreator = new ImgCreator();
            BufferedImage newImage = imgCreator.newImage(maze.getMaze());
            img.setImage(SwingFXUtils.toFXImage(newImage, null));
            chekClick = false;
        } else {
            WarnAlert.alertSolve();
        }

    }

    @FXML
    protected void newMaze() {
        if (Integer.parseInt(height.getText()) > 3 && Integer.parseInt(width.getText()) > 3) {
            maze = new Maze(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()));
            maze.createMaze();
            ImgCreator imgCreator = new ImgCreator();
            BufferedImage newImage = imgCreator.newImage(maze.getMaze());
            img.setImage(SwingFXUtils.toFXImage(newImage, null));
            chekClick = true;
        } else {
            WarnAlert.warnAlert();
        }


    }
}