package com.nikolai.mazesolver.view;

import com.nikolai.mazesolver.model.Cell;
import com.nikolai.mazesolver.model.Maze;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Arrays;
import java.util.Objects;

public class ImgCreator {

    private static final int PIXEL_SIZE = 3;
    private static final int IMAGE_TYPE = 5;

    public BufferedImage newImage(Cell[][] maze) {
        int width = maze[0].length;
        int height = maze.length;

        BufferedImage result = new BufferedImage(width * PIXEL_SIZE, height * PIXEL_SIZE, IMAGE_TYPE);
        for (int x = 0; x < height; x++) {
            for (int y = 0; y < width; y++) {
                for (int pixelX = 0; pixelX < PIXEL_SIZE; pixelX++) {
                    for (int pixelY = 0; pixelY < PIXEL_SIZE; pixelY++) {
                        //System.out.println(x + " | " + y);
                        Color color = colorCell(maze[x][y]);
                        result.setRGB(y * PIXEL_SIZE + pixelY, x * PIXEL_SIZE + pixelX, color.getRGB());
                    }
                }
            }
        }
        return result;
    }


    public Color colorCell(Cell cell) {
        Color color = new Color(0, 0, 0);
        if (cell.getValue() == 1) {
            color = Color.BLACK;
        }
        if (cell.getValue() == 0) {
            color = Color.WHITE;
        }
        if (cell.getValue() == 2) {
            color = Color.GREEN;
        }
        if (cell.getValue() == 3) {
            color = Color.RED;
        }
        if (cell.getValue() == -1) {
            color = Color.PINK;
        }
        return color;
    }

}
