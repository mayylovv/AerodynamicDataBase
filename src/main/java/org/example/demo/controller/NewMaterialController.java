package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.repository.MaterialInfoRepository;

public class NewMaterialController {

    private final MaterialInfoRepository materialInfoRepository;

    public NewMaterialController(MaterialInfoRepository materialInfoRepository) {
        this.materialInfoRepository = materialInfoRepository;
    }

    // Метод для установки Stage
    @Setter
    private Stage stage; // Добавляем ссылку на Stage

    @FXML
    private TextField newMaterialName, newMaterialDensity;

    @FXML
    private Label newMaterialLog;

    @FXML
    private Button newMaterialSaveButton, cancelButton;

    @FXML
    public void saveNewMaterial() {

        String name = newMaterialName.getText();
        String density = newMaterialDensity.getText();
        if (name.isBlank() || density.isBlank()) {
            newMaterialLog.setText("Заполнены не все поля");
            return;
        }
        if (materialInfoRepository.existsByName(name)) {
            newMaterialLog.setText("Материал с таким именем уже существует");
            return;
        }

        MaterialInfoEntity materialInfo = new MaterialInfoEntity();
        try {
            materialInfo.setName(name);
            materialInfo.setDensity(convertStringToDouble(density));
        } catch (NumberFormatException e) {
            newMaterialLog.setText("Некорректный формат данных, запись с name = " + name + " не сохранена");
            return;
        }
        materialInfoRepository.save(materialInfo);
        newMaterialLog.setText("Запись с name " + name + " успешно сохранена");

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
