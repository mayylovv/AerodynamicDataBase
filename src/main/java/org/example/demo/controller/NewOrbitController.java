package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.demo.entity.OrbitCharacteristics;
import org.example.demo.repository.OrbitCharacteristicsRepository;

public class NewOrbitController {

    private final OrbitCharacteristicsRepository orbitCharacteristicsRepository;

    public NewOrbitController(OrbitCharacteristicsRepository orbitCharacteristicsRepository) {
        this.orbitCharacteristicsRepository = orbitCharacteristicsRepository;
    }

    @Setter
    private Stage stage;

    @FXML
    private TextField newOrbit, newSpeed, newAlfa;

    @FXML
    private Label newOrbitLog;

    @FXML
    private Button newOrbitSaveButton, cancelButton;

    @FXML
    private void saveNewOrbit() {
        String orbit = newOrbit.getText();
        String speed = newSpeed.getText();
        String alfa = newAlfa.getText();
        if (orbit.isBlank() || speed.isBlank() || alfa.isBlank()) {
            newOrbitLog.setText("Заполнены не все поля");
            return;
        }
        OrbitCharacteristics orbitChar = new OrbitCharacteristics();
        try {
            orbitChar.setOrbit(convertStringToDouble(orbit));
            orbitChar.setSpeed(convertStringToDouble(speed));
            orbitChar.setAlfa(convertStringToDouble(alfa));
        } catch (NumberFormatException e) {
            newOrbitLog.setText("Некорректный формат данных, запись с орбитой = " + orbit + " не сохранена");
            return;
        }
        orbitCharacteristicsRepository.save(orbitChar);
        newOrbitLog.setText("Запись с орбитой " + orbit + " успешно сохранена");

        if (stage != null) {
            stage.close();
        }
    }

    @FXML
    public void cancelAction() {
        if (stage != null) {
            stage.close();
        }
    }

    private static double convertStringToDouble(String numberString) {
        if (numberString.contains(",")) {
            numberString = numberString.replace(",", ".");
        }
        double resultDouble = Double.parseDouble(numberString);
        if (resultDouble < 0) {
            throw new NumberFormatException();
        }
        return resultDouble;
    }
}
