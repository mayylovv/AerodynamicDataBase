package org.example.demo;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.RequiredArgsConstructor;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.FormNtu;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.service.InitializationService;
import org.example.demo.service.MainCalculationService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Controller {

    private final InitializationService initializationService;
    private final MainCalculationService calculationService;

    private double speed;
    private double angleOfAttack;
    private int heightKm;
    private int charNtuId;
    private int cubesatSizeId;

    @FXML
    private ChoiceBox<String> flightHeightChoiceBox;

    @FXML
    private ChoiceBox<String> shapeChoiceBox;

    @FXML
    private ChoiceBox<String> materialChoiceBox;

    @FXML
    private TextField nameField, lengthField, widthField, heightField;

    @FXML
    private TextField xMassField, yMassField, zMassField, massField;

    @FXML
    private TextField radiusField, shapeLengthField, thicknessField;

    @FXML
    private TextField newMaterialNameField, newMaterialDensityField;

    @FXML
    private TextField spacecraftSpeedField, attackAngleField;

    @FXML
    private Button saveCubeSatButton, saveShapeButton, saveMaterialButton, saveFlightParamsButton, calculateButton;

    @FXML
    private TextArea resultTextArea;

    @FXML
    private void initialize() {
        List<String> listHeight = initializationService.getAllHeight();
        flightHeightChoiceBox.getItems().addAll(listHeight);
        List<String> listForm = initializationService.getAllShapes();
        shapeChoiceBox.getItems().addAll(listForm);
        List<String> listMaterial = initializationService.getAllMaterial();
        materialChoiceBox.getItems().addAll(listMaterial);
    }

    @FXML
    private void saveCubeSat() {
        System.out.println("Сохранение характеристик кубсата: " + nameField.getText());
        CubesatSize newCubesat = new CubesatSize();
        newCubesat.setName(nameField.getText());
        newCubesat.setLength(convertStringToDouble(lengthField.getText()));
        newCubesat.setWidth(convertStringToDouble(widthField.getText()));
        newCubesat.setHeight(convertStringToDouble(heightField.getText()));
        newCubesat.setXMass(convertStringToDouble(xMassField.getText()));
        newCubesat.setYMass(convertStringToDouble(yMassField.getText()));
        newCubesat.setZMass(convertStringToDouble(zMassField.getText()));
        newCubesat.setMass(convertStringToDouble(massField.getText()));
        CubesatSize cubesatSize = initializationService.saveCubesatSize(newCubesat);
        cubesatSizeId = cubesatSize.getId();
    }

    @FXML
    private void saveShape() {
        System.out.println("Сохранение характеристик надувной оболочки");
        CharacteristicsNtu newNtu = new CharacteristicsNtu();
        newNtu.setFormId(initializationService.getIdFormByName(shapeChoiceBox.getValue()));
        newNtu.setRadius(convertStringToDouble(radiusField.getText()));
        newNtu.setLength(convertStringToDouble(shapeLengthField.getText()));
        newNtu.setThickness(convertStringToDouble(thicknessField.getText()));
        newNtu.setMaterialId(initializationService.getIdMaterialByName(materialChoiceBox.getValue()));
        CharacteristicsNtu characteristicsNtu = initializationService.saveCharacteristicsNtu(newNtu);
        charNtuId = characteristicsNtu.getId();
    }

    @FXML
    private void saveMaterial() {
        System.out.println("Добавление нового материала: " + newMaterialNameField.getText());
        MaterialInfoEntity newMaterial = new MaterialInfoEntity();
        newMaterial.setName(newMaterialNameField.getText());
        newMaterial.setDensity(convertStringToDouble(newMaterialDensityField.getText()));
        initializationService.saveMaterialInfo(newMaterial);
    }

    @FXML
    private void saveFlightParams() {
        System.out.println("Сохранение параметров полета");
        speed = convertStringToDouble(spacecraftSpeedField.getText());
        angleOfAttack = convertStringToDouble(attackAngleField.getText());
        heightKm = (int) convertStringToDouble(flightHeightChoiceBox.getValue());
        System.out.println("Скорость = " + speed + "  Угол атаки = " + angleOfAttack + " градусов, Высота = " + heightKm);
    }

    @FXML
    private void calculate() {

        CubesatSize cubesatSize = initializationService.getCubesatById(cubesatSizeId);
        CharacteristicsNtu characteristicsNtu = initializationService.getCharacteristicsById(charNtuId);
        FormNtu formNtu = initializationService.getFormById(characteristicsNtu.getFormId());

        double radius = characteristicsNtu.getRadius();
        double lengthNtu = characteristicsNtu.getLength();
        double alfa = angleOfAttack * (Math.PI / 180.0);
        String formName = formNtu.getFileName();

        System.out.println("Выполнение расчета аэродинамики");

        String forceX = String.valueOf(calculationService.calculateForceX(heightKm, radius, lengthNtu, alfa, formName, speed));
        String momentX = String.valueOf(calculationService.calculateMomentX(heightKm, radius, alfa, lengthNtu, speed, formName, cubesatSize, characteristicsNtu));
        String coefficientX = String.valueOf(calculationService.calculateCoefficientX(radius, lengthNtu, alfa, formName));

        String forceY = String.valueOf(calculationService.calculateForceY(heightKm, radius, lengthNtu, alfa, formName, speed));
        String momentY = String.valueOf(calculationService.calculateMomentY(heightKm, radius, alfa, lengthNtu, speed, formName, cubesatSize, characteristicsNtu));
        String coefficientY = String.valueOf(calculationService.calculateCoefficientY(alfa));

        String velocityHead = String.valueOf(calculationService.calculateVelocityHead(heightKm, speed));

        resultTextArea.setText(
                String.format(result, forceX, momentX, coefficientX, forceY, momentY, coefficientY, velocityHead)
        );


    }

    private static double convertStringToDouble(String numberString) {
        try {
            if (numberString.contains(",")) {
                numberString = numberString.replace(",", ".");
            }
            return Double.parseDouble(numberString); // Преобразуем строку в double
        } catch (NumberFormatException e) {
            System.out.println("Некорректный формат строки для преобразования в Double: " + e.getMessage());
            return 0.0;
        }
    }

    private String result = """
            Лобовое сопративление: %s \n
            Кренящий момент: %s \n
            Коэффициент лобового сопративления: %s \n
            Подъемная сила: %s \n
            Тангажный момент: %s \n
            Коэффициент подъемной силы: %s \n
            Скоростной напор: %s \n
             
            """;
}
