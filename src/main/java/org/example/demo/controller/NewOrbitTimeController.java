package org.example.demo.controller;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.demo.entity.OrbitCharacteristicsTime;
import org.example.demo.repository.OrbitCharacteristicsTimeRepository;

public class NewOrbitTimeController {

    private final OrbitCharacteristicsTimeRepository orbitCharacteristicsTimeRepository;

    public NewOrbitTimeController(OrbitCharacteristicsTimeRepository orbitCharacteristicsTimeRepository) {
        this.orbitCharacteristicsTimeRepository = orbitCharacteristicsTimeRepository;
    }

    @Setter
    private Stage stage;

    @FXML
    private TextField newInitOrbit, newFinalOrbit, newSpeedTime, newAlfaTime, newGammaTime;

    @FXML
    private Label newOrbitTimeLog;

    @FXML
    private Button newMaterialSaveButton, cancelButton;

    @FXML
    private void saveNewOrbitTime() {
        String initOrbit = newInitOrbit.getText();
        String finalOrbit = newFinalOrbit.getText();
        String speed = newSpeedTime.getText();
        String alfa = newAlfaTime.getText();
        String gamma = newGammaTime.getText();
        if (initOrbit.isBlank() || finalOrbit.isBlank() || speed.isBlank() || alfa.isBlank() || gamma.isBlank()) {
            newOrbitTimeLog.setText("Заполнены не все поля");
            return;
        }
        OrbitCharacteristicsTime orbitChar = new OrbitCharacteristicsTime();
        try {
            orbitChar.setInitialOrbit(convertStringToDouble(initOrbit));
            orbitChar.setFinalOrbit(convertStringToDouble(finalOrbit));
            orbitChar.setSpeed(convertStringToDouble(speed));
            orbitChar.setAlfa(convertStringToDouble(alfa));
            orbitChar.setGamma(convertStringToDouble(gamma));
        } catch (NumberFormatException e) {
            newOrbitTimeLog.setText("Некорректный формат данных, запись не сохранена");
            return;
        }
        orbitCharacteristicsTimeRepository.save(orbitChar);
        newOrbitTimeLog.setText("Запись  успешно сохранена");

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
