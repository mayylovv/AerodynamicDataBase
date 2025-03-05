package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.FormNtu;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.FormNtuRepository;
import org.example.demo.repository.MaterialInfoRepository;

import java.util.List;

public class NewNtuController {

    private final CharacteristicsNtuRepository characteristicsNtuRepository;
    private final FormNtuRepository formNtuRepository;
    private final MaterialInfoRepository materialInfoRepository;

    public NewNtuController(CharacteristicsNtuRepository characteristicsNtuRepository, FormNtuRepository formNtuRepository,
                            MaterialInfoRepository materialInfoRepository) {
        this.characteristicsNtuRepository = characteristicsNtuRepository;
        this.formNtuRepository = formNtuRepository;
        this.materialInfoRepository = materialInfoRepository;

    }

    @Setter
    private Stage stage;

    @FXML
    private TableView<MaterialInfoEntity> materialTableView;

    @FXML
    private TableColumn<MaterialInfoEntity, String> nameMaterialColumn;

    @FXML
    private TableColumn<MaterialInfoEntity, Double> densityMaterialColumn;

    @FXML
    private Label newNtuLog;

    @FXML
    private ChoiceBox<String> formChoiceBox, materialChoiceBox;

    @FXML
    private TextField nameNewNtu, radiusNewNtu, lengthNewNtu, thicknessNewNtu;

    @FXML
    private Button saveNtuButton, cancelButton;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadTableData();
        loadInitialChoiceBox();
    }

    @FXML
    public void saveNewNtu() {
        String name = nameNewNtu.getText();
        String formName = formChoiceBox.getValue();
        String radius = radiusNewNtu.getText();
        String length = lengthNewNtu.getText();
        String thickness = thicknessNewNtu.getText();
        String materialName = materialChoiceBox.getValue();
        if (isNullOrBlank(name) || isNullOrBlank(formName) ||
                isNullOrBlank(radius) || isNullOrBlank(length) ||
                isNullOrBlank(thickness) || isNullOrBlank(materialName)) {
            newNtuLog.setText("Заполнены не все поля");
            return;
        }
        if (characteristicsNtuRepository.existsByName(name)) {
            newNtuLog.setText("Оболочка с таким именем уже существует");
            return;
        }
        CharacteristicsNtu characteristicsNtu = new CharacteristicsNtu();
        FormNtu formNtu = formNtuRepository.findByName(formName).get();
        MaterialInfoEntity materialInfo = materialInfoRepository.findByName(materialName).get();
        try {
            characteristicsNtu.setName(name);
            characteristicsNtu.setForm(formNtu);
            characteristicsNtu.setRadius(convertStringToDouble(radius));
            characteristicsNtu.setLength(convertStringToDouble(length));
            characteristicsNtu.setThickness(convertStringToDouble(thickness));
            characteristicsNtu.setMaterial(materialInfo);
        } catch (NumberFormatException e) {
            newNtuLog.setText("Некорректный формат данных, запись с name = " + name + " не сохранена");
            return;
        }
        characteristicsNtuRepository.save(characteristicsNtu);
        newNtuLog.setText("Запись с name " + name + " успешно сохранена");
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

    private boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
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

    private void setupTableColumns() {
        nameMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        densityMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("density"));
    }

    private void loadTableData() {
        List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
        materialTableView.setItems(FXCollections.observableArrayList(materialData));
    }

    private void loadInitialChoiceBox() {
        List<String> materialNameList = materialInfoRepository.findAll().stream().map(MaterialInfoEntity::getName).toList();
        materialChoiceBox.getItems().addAll(materialNameList);

        List<String> formNameList = formNtuRepository.findAll().stream().map(FormNtu::getName).toList();
        formChoiceBox.getItems().addAll(formNameList);
    }
}
