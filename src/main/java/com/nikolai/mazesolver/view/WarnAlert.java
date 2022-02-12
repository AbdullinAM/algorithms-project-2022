package com.nikolai.mazesolver.view;

import javafx.scene.control.Alert;

public class WarnAlert {

    private static final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static void alertSolve() {
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText("Перед тем как решить лабиринт, сгенерируйте его");
        alert.show();
    }

    public static void warnAlert() {
        alert.setTitle("Game over!!!!!");
        alert.setHeaderText(null);
        alert.setContentText("Для корректной генерации лабиринта высота и ширина должны быть равны минимум 4");
        alert.show();
    }

}
