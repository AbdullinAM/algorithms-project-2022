package com.nikolai.mazesolver.controller;

import com.nikolai.mazesolver.model.Cell;
import com.nikolai.mazesolver.model.MazeSolver;
import com.nikolai.mazesolver.view.ImgCreator;
import com.nikolai.mazesolver.model.Maze;
import com.nikolai.mazesolver.view.WarnAlert;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.util.Deque;

public class Controller {
    public javafx.scene.control.TextField width;
    public javafx.scene.control.TextField height;
    @FXML
    private ImageView img;

    private Maze maze;
    private ScrollPane scrollPane;

    @FXML
    void initialize() {
        scrollPane= new ScrollPane();
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPannable(true);
    }

    @FXML
    protected void solveMaze() {
        MazeSolver mazeSolver = new MazeSolver();
        Deque<Cell> path = mazeSolver.solve(maze);
        maze.addPath(path);
        ImgCreator imgCreator = new ImgCreator();
        BufferedImage newImage = imgCreator.newImage(maze.getMaze());

        img.setImage(SwingFXUtils.toFXImage(newImage, null));
    }

    @FXML
    protected void newMaze() {
        if(Integer.parseInt(height.getText())>2 && Integer.parseInt(width.getText())>2){
            maze = new Maze(Integer.parseInt(height.getText()), Integer.parseInt(width.getText()));
            maze.createMaze();
            ImgCreator imgCreator = new ImgCreator();
            BufferedImage newImage = imgCreator.newImage(maze.getMaze());
            img.setImage(SwingFXUtils.toFXImage(newImage, null));
            scrollPane.setContent(img);
        }else {
            WarnAlert.alertWarn();
        }

    }
}