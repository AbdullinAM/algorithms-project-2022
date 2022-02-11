package com.nikolai.mazesolver.view;

import javafx.scene.control.Alert;

public class WarnAlert {
    private static final Alert alert = new Alert(Alert.AlertType.INFORMATION);

    public static void alertWarn() {
        alert.setTitle("Warn!!!");
        alert.setHeaderText(null);
        alert.setContentText("Для корректной генерации алгоритма высота и ширина должны быть больше 2");
        alert.show();
    }
}
