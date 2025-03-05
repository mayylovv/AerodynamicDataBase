package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.demo.entity.CubesatSize;
import org.example.demo.repository.CubesatSizeRepository;


public class NewCubesatController {

    private final CubesatSizeRepository cubesatSizeRepository;

    public NewCubesatController(CubesatSizeRepository cubesatSizeRepository) {
        this.cubesatSizeRepository = cubesatSizeRepository;
    }


    // Метод для установки Stage
    @Setter
    private Stage stage; // Добавляем ссылку на Stage

    @FXML
    private TextField nameNewCubesat, lengthNewCubesat, widthNewCubesat, heightNewCubesat, massNewCubesat;

    @FXML
    private TextField xMassNewCubesat, yMassNewCubesat, zMassNewCubesat;

    @FXML
    private Label newCubesatLog;

    @FXML
    private Button saveCubesat, cancelButton;


    @FXML
    public void saveNewCubesat() {
        String name = nameNewCubesat.getText();
        String length = lengthNewCubesat.getText();
        String width = widthNewCubesat.getText();
        String height = heightNewCubesat.getText();
        String xMass = xMassNewCubesat.getText();
        String yMass = yMassNewCubesat.getText();
        String zMass = zMassNewCubesat.getText();
        String mass = massNewCubesat.getText();
        if (name.isBlank() || length.isBlank() || width.isBlank() || height.isBlank() || xMass.isBlank() || yMass.isBlank() || zMass.isBlank() || mass.isBlank()) {
            newCubesatLog.setText("Заполнены не все поля");
            return;
        }
        if (cubesatSizeRepository.existsByName(name)) {
            newCubesatLog.setText("Аппарат с таким именем уже существует");
            return;
        }
        CubesatSize cubesatSize = new CubesatSize();
        try {
            cubesatSize.setName(name);
            cubesatSize.setLength(convertStringToDouble(length));
            cubesatSize.setWidth(convertStringToDouble(width));
            cubesatSize.setHeight(convertStringToDouble(height));
            cubesatSize.setXMass(convertStringToDouble(xMass));
            cubesatSize.setYMass(convertStringToDouble(yMass));
            cubesatSize.setZMass(convertStringToDouble(zMass));
            cubesatSize.setMass(convertStringToDouble(mass));
        } catch (NumberFormatException e) {
            newCubesatLog.setText("Некорректный формат данных, запись с name = " + name + " не сохранена");
            return;
        }
        cubesatSizeRepository.save(cubesatSize);
        newCubesatLog.setText("Запись с name " + name + " успешно сохранена");

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
