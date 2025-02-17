package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.stereotype.Component;

@Component
public class Controller {
    @FXML
    private Label helloLabel;

    public void initialize() {
        helloLabel.setText("Привет из Spring + JavaFX!");
    }
}
