package org.example.the_majestic_haven_midterm.Singleton;

import javafx.scene.layout.StackPane;

public class SharedStackPane {

    private static int c = 0;
    private static SharedStackPane instance;
    private StackPane parentStackPane;

    private SharedStackPane() {}

    public static SharedStackPane getInstance() {
        if (c == 0) {
            c++;
            instance = new SharedStackPane();
        }
        return instance;
    }

    public void setParentStackPane(StackPane parentStackPane) {
        this.parentStackPane = parentStackPane;
    }

    public StackPane getParentStackPane() {
        return parentStackPane;
    }
}
