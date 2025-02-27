package org.example.demo;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.FlightCharacteristics;
import org.example.demo.dto.NtuTableDto;
import org.example.demo.entity.AerodynamicCharacteristics;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.FormNtu;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.repository.AerodynamicCharacteristicsRepository;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.CubesatSizeRepository;
import org.example.demo.repository.FormNtuRepository;
import org.example.demo.repository.MaterialInfoRepository;
import org.example.demo.service.InitializationService;
import org.example.demo.service.MainCalculationService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Controller {

    private final CubesatSizeRepository cubesatSizeRepository;
    private final MaterialInfoRepository materialInfoRepository;
    private final FormNtuRepository formNtuRepository;
    private final CharacteristicsNtuRepository characteristicsNtuRepository;
    private final InitializationService initializationService;
    private final MainCalculationService calculationService;
    private final AerodynamicCharacteristicsRepository aerodynamicCharacteristicsRepository;

    private CubesatSize actualCubesatSize;
    private CharacteristicsNtu actualCharacteristicsNtu;
    private double actualHeightKm;
    private double actualAlfa;
    private double actualSpeed;
    private FlightCharacteristics actualFlightChar;

    // все что есть
    @FXML
    private ChoiceBox<String> cubesatChoiceBox, ntuChoiceBox, formChoiceBox, materialChoiceBox, deleteCubesatChoiceBox,
            deleteNtuChoiceBox, deleteMaterialChoiceBox;

    @FXML
    public ScrollPane cubesatScroller;

    @FXML
    private TableView<NtuTableDto> ntuTableView;

    @FXML
    private TableColumn<NtuTableDto, String> nameNtuColumn, formNtuColumn, materialNtuColumn;

    @FXML
    private TableColumn<NtuTableDto, Double> radiusNtuColumn, lengthNtuColumn, thicknessNtuColumn, densityNtuColumn;

    @FXML
    private TableView<CubesatSize> cubesatTableView;

    @FXML
    private TableColumn<CubesatSize, String> nameColumn;

    @FXML
    private TableColumn<CubesatSize, Double> lengthColumn, widthColumn, heightColumn, xMassColumn, yMassColumn, zMassColumn, massColumn;

    @FXML
    private TableView<MaterialInfoEntity> materialTableView;

    @FXML
    private TableColumn<MaterialInfoEntity, String> nameMaterialColumn;

    @FXML
    private TableColumn<MaterialInfoEntity, Double> densityMaterialColumn;

    @FXML
    private Button chooseNtu, chooseCubesat, chooseFlight;

    @FXML
    private Button saveCubesat, saveNtu, saveMaterial;

    @FXML
    private Button calculateButton, getCalculatesButton;

    @FXML
    private Button deleteCubesatButton, deleteNtuButton, deleteMaterialButton;

    // выбранные нту и ка
    @FXML
    private TextField chosenNtu, chosenCubesat;

    @FXML
    private TextField heightFlight, speedFlight, angleFlight;

    @FXML
    private TextField nameNewMaterial, newDensityMaterial;

    @FXML
    private TextField nameNewCubesat, lengthNewCubesat, widthNewCubesat, heightNewCubesat, xMassNewCubesat, yMassNewCubesat, zMassNewCubesat, massNewCubesat;

    @FXML
    private TextField nameNewNtu, radiusNewNtu, lengthNewNtu, thicknessNewNtu;

    // условно логи
    @FXML
    private Label flightLog, newCubesatLog, newNtuLog, newMaterialLog, deleteCubesatLog, deleteNtuLog, deleteMaterialLog;

    // информация с характеристиками полета
    @FXML
    private TextArea flightArea;

    @FXML
    private TextArea resultTextArea;


    @FXML
    public void initialize() {
        setupTableColumns();
        loadTableData();
        loadInitialChoiceBox();
    }

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

        List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
        cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));

        List<String> cubesatNameList = cubesatData.stream().map(CubesatSize::getName).toList();
        deleteCubesatChoiceBox.getItems().clear();
        deleteCubesatChoiceBox.getItems().addAll(cubesatNameList);
        cubesatChoiceBox.getItems().clear();
        cubesatChoiceBox.getItems().addAll(cubesatNameList);
    }

    @FXML
    public void chooseCubesatSize() {
        String cubesatName = cubesatChoiceBox.getValue();
        actualCubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
        chosenCubesat.setText(cubesatName);
    }

    @FXML
    public void deleteCubesatSize() {
        try {
            String cubesatName = deleteCubesatChoiceBox.getValue();
            CubesatSize cubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
            cubesatSizeRepository.delete(cubesatSize);
            deleteCubesatLog.setText("Запись с name " + cubesatName + " успешно удалена");

            List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
            cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));

            List<String> cubesatNameList = cubesatData.stream().map(CubesatSize::getName).toList();
            deleteCubesatChoiceBox.getItems().clear();
            deleteCubesatChoiceBox.getItems().addAll(cubesatNameList);
            cubesatChoiceBox.getItems().clear();
            cubesatChoiceBox.getItems().addAll(cubesatNameList);
        } catch (Exception e) {
            deleteCubesatLog.setText("Запись "+ deleteCubesatChoiceBox.getValue() + " не удалена по неизвестной причине");
        }
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

        List<NtuTableDto> ntuData = getAllForTable();
        ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        List<String> ntuNameList = characteristicsNtuRepository.findAll().stream().map(CharacteristicsNtu::getName).toList();
        deleteNtuChoiceBox.getItems().clear();
        deleteNtuChoiceBox.getItems().addAll(ntuNameList);
        ntuChoiceBox.getItems().clear();
        ntuChoiceBox.getItems().addAll(ntuNameList);
    }

    @FXML
    private void deleteNtu() {
        try {
            String ntuName = deleteNtuChoiceBox.getValue();
            CharacteristicsNtu ntu = characteristicsNtuRepository.findByName(ntuName).get();
            characteristicsNtuRepository.delete(ntu);
            deleteNtuLog.setText("Запись с name " + ntuName + " успешно удалена");

            List<NtuTableDto> ntuData = getAllForTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

            List<String> ntuNameList = characteristicsNtuRepository.findAll().stream().map(CharacteristicsNtu::getName).toList();
            deleteNtuChoiceBox.getItems().clear();
            deleteNtuChoiceBox.getItems().addAll(ntuNameList);
            ntuChoiceBox.getItems().clear();
            ntuChoiceBox.getItems().addAll(ntuNameList);

        } catch (Exception e) {
            deleteCubesatLog.setText("Запись " + deleteNtuChoiceBox.getValue() + "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void saveNewMaterial() {
        String name = nameNewMaterial.getText();
        String density = newDensityMaterial.getText();
        if (name.isBlank() || density.isBlank()) {
            newMaterialLog.setText("Заполнены не все поля");
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


        List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
        materialTableView.setItems(FXCollections.observableArrayList(materialData));

        List<NtuTableDto> ntuData = getAllForTable();
        ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        List<String> materialNameList = materialInfoRepository.findAll().stream().map(MaterialInfoEntity::getName).toList();
        deleteMaterialChoiceBox.getItems().clear();
        deleteMaterialChoiceBox.getItems().addAll(materialNameList);
        materialChoiceBox.getItems().clear();
        materialChoiceBox.getItems().addAll(materialNameList);

        List<String> ntuNameList = characteristicsNtuRepository.findAll().stream().map(CharacteristicsNtu::getName).toList();
        deleteNtuChoiceBox.getItems().addAll(ntuNameList);
        ntuChoiceBox.getItems().addAll(ntuNameList);
    }

    @FXML
    private void deleteMaterial() {
        try {
            String materialName = deleteMaterialChoiceBox.getValue();
            MaterialInfoEntity material = materialInfoRepository.findByName(materialName).get();
            List<CharacteristicsNtu> ntus = characteristicsNtuRepository.findAllByMaterial(material);
            characteristicsNtuRepository.deleteAll(ntus);
            materialInfoRepository.delete(material);
            deleteMaterialLog.setText("Запись с name " + materialName + " успешно удалена");


            List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
            materialTableView.setItems(FXCollections.observableArrayList(materialData));

            List<NtuTableDto> ntuData = getAllForTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

            List<String> materialNameList = materialInfoRepository.findAll().stream().map(MaterialInfoEntity::getName).toList();
            deleteMaterialChoiceBox.getItems().clear();
            deleteMaterialChoiceBox.getItems().addAll(materialNameList);
            materialChoiceBox.getItems().clear();
            materialChoiceBox.getItems().addAll(materialNameList);

            List<String> ntuNameList = characteristicsNtuRepository.findAll().stream().map(CharacteristicsNtu::getName).toList();
            deleteNtuChoiceBox.getItems().clear();
            deleteNtuChoiceBox.getItems().addAll(ntuNameList);
            ntuChoiceBox.getItems().clear();
            ntuChoiceBox.getItems().addAll(ntuNameList);
        } catch (Exception e) {
            deleteMaterialLog.setText("Запись " + deleteMaterialChoiceBox.getValue() + "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void chooseNtuSize() {
        String ntuName = ntuChoiceBox.getValue();
        actualCharacteristicsNtu = characteristicsNtuRepository.findByName(ntuName).get();
        chosenNtu.setText(ntuName);

    }

    @FXML
    private void chooseFlightChar() {
        String heightKm = heightFlight.getText();
        String alfa = angleFlight.getText();
        String speed = speedFlight.getText();
        if (heightKm.isBlank() || alfa.isBlank() || speed.isBlank()) {
            flightLog.setText("Заполнены не все поля");
            return;
        }
        try {
            actualHeightKm = convertStringToDouble(heightKm);
            actualAlfa = convertStringToDoubleWithMinus(alfa);
            actualSpeed = convertStringToDouble(speed);
            FlightCharacteristics flightCharacteristics = new FlightCharacteristics();
            flightCharacteristics.setHeightKm(actualHeightKm);
            flightCharacteristics.setAlfa(actualAlfa);
            flightCharacteristics.setSpeed(actualSpeed);
            actualFlightChar = flightCharacteristics;
            flightArea.setText(String.format(flightChar, actualHeightKm, actualAlfa, actualSpeed));
        } catch (NumberFormatException e) {
            newMaterialLog.setText("Некорректный формат данных");
        }
    }


    @FXML
    private void calculate() {
        if (actualCharacteristicsNtu == null || actualCubesatSize == null || actualFlightChar == null) {
            resultTextArea.setText("Не все поля заполнены");
        } else {
            double alfa = actualAlfa * (Math.PI / 180); // тут радианы
            double speed = actualSpeed; // тут метры в секунду
            double radius = actualCharacteristicsNtu.getRadius();
            double length = actualCharacteristicsNtu.getLength();
            String formName = actualCharacteristicsNtu.getForm().getFileName();

            System.out.println(alfa);
            System.out.println(speed);
            System.out.println(radius);
            System.out.println(length);
            System.out.println(formName);

            double forceX = calculationService.calculateForceX(actualHeightKm, radius, length, formName, speed);
            double forceY = calculationService.calculateForceY(actualHeightKm, radius, length, alfa, formName, speed);
            double momentX = calculationService.calculateMomentX(actualHeightKm, radius, length, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double momentY = calculationService.calculateMomentY(actualHeightKm, radius, alfa, length, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double coefficientX = calculationService.calculateCoefficientX(radius, length, formName);
            double coefficientY = calculationService.calculateCoefficientY(alfa);
            double velocityHead = calculationService.calculateVelocityHead(actualHeightKm, speed);
            double density = calculationService.getDensity(actualHeightKm);
            double minSpeed = calculationService.calculateMinSpeed(actualHeightKm);

            String strForceX = String.valueOf(forceX);
            String strForceY = String.valueOf(forceY);
            String strMomentX = String.valueOf(momentX);
            String strMomentY = String.valueOf(momentY);
            String strCoefficientX = String.valueOf(coefficientX);
            String strCoefficientY = String.valueOf(coefficientY);
            String strVelocityHead = String.valueOf(velocityHead); // тут


            resultTextArea.setText(
                    String.format(result, strForceX, strMomentX, strCoefficientX, strForceY, strMomentY, strCoefficientY, strVelocityHead)
            );

            AerodynamicCharacteristics aeroChar = new AerodynamicCharacteristics();
            aeroChar.setAlfa(alfa);
            aeroChar.setCubesatSizeId(actualCubesatSize.getId());
            aeroChar.setNtuId(actualCharacteristicsNtu.getId());
            aeroChar.setForceX(forceX);
            aeroChar.setMomentX(momentX);
            aeroChar.setForceY(forceY);
            aeroChar.setMomentY(momentY);
            aeroChar.setCoefficientX(coefficientX);
            aeroChar.setCoefficientY(coefficientY);
            aeroChar.setVelocityHead(velocityHead);
            aeroChar.setDateOfCalculation(LocalDateTime.now());
            aeroChar.setDensity(density);
            aeroChar.setSpeed(speed);
            aeroChar.setMinSpeed(minSpeed);

            aerodynamicCharacteristicsRepository.save(aeroChar);

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

    private static double convertStringToDoubleWithMinus(String numberString) {
        if (numberString.contains(",")) {
            numberString = numberString.replace(",", ".");
        }
        double resultDouble = Double.parseDouble(numberString);
        return resultDouble;
    }

    private void loadTableData() {
        List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
        cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));

        List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
        materialTableView.setItems(FXCollections.observableArrayList(materialData));

        List<NtuTableDto> ntuData = getAllForTable();
        ntuTableView.setItems(FXCollections.observableArrayList(ntuData));
    }

    private void setupTableColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        lengthColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        widthColumn.setCellValueFactory(new PropertyValueFactory<>("width"));
        heightColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        xMassColumn.setCellValueFactory(new PropertyValueFactory<>("xMass"));
        yMassColumn.setCellValueFactory(new PropertyValueFactory<>("yMass"));
        zMassColumn.setCellValueFactory(new PropertyValueFactory<>("zMass"));
        massColumn.setCellValueFactory(new PropertyValueFactory<>("mass"));

        nameNtuColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        formNtuColumn.setCellValueFactory(new PropertyValueFactory<>("form"));
        radiusNtuColumn.setCellValueFactory(new PropertyValueFactory<>("radius"));
        lengthNtuColumn.setCellValueFactory(new PropertyValueFactory<>("length"));
        thicknessNtuColumn.setCellValueFactory(new PropertyValueFactory<>("thickness"));
        materialNtuColumn.setCellValueFactory(new PropertyValueFactory<>("material"));
        densityNtuColumn.setCellValueFactory(new PropertyValueFactory<>("density"));

        nameMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        densityMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("density"));
    }

    private void loadInitialChoiceBox() {
        List<String> cubesatNameList = cubesatSizeRepository.findAll().stream().map(CubesatSize::getName).toList();
        deleteCubesatChoiceBox.getItems().addAll(cubesatNameList);
        cubesatChoiceBox.getItems().addAll(cubesatNameList);

        List<String> materialNameList = materialInfoRepository.findAll().stream().map(MaterialInfoEntity::getName).toList();
        deleteMaterialChoiceBox.getItems().addAll(materialNameList);
        materialChoiceBox.getItems().addAll(materialNameList);

        List<String> ntuNameList = characteristicsNtuRepository.findAll().stream().map(CharacteristicsNtu::getName).toList();
        deleteNtuChoiceBox.getItems().addAll(ntuNameList);
        ntuChoiceBox.getItems().addAll(ntuNameList);

        List<String> formNameList = formNtuRepository.findAll().stream().map(FormNtu::getName).toList();
        formChoiceBox.getItems().addAll(formNameList);
    }

    private boolean isNullOrBlank(String str) {
        return str == null || str.isBlank();
    }

    public List<NtuTableDto> getAllForTable() {
        List<Object[]> rawData = formNtuRepository.getAllForTable();
        List<NtuTableDto> dtos = new ArrayList<>();

        for (Object[] objects : rawData) {
            NtuTableDto dto = new NtuTableDto(
                    (Integer) objects[0], // ntu.id
                    (String) objects[1],  // ntu.name
                    (String) objects[2],  // form.name
                    (Double) objects[3],  // ntu.radius
                    (Double) objects[4],  // ntu.length
                    (Double) objects[5],  // ntu.thickness
                    (String) objects[6],  // material_info.name
                    (Double) objects[7]   // material_info.density
            );
            dtos.add(dto);
        }
        return dtos;
    }

    private String flightChar = """
            Высота полета: %s  км
                       \s
            Угол атаки: %s  градусов
                       \s
            Скорость: %s м/с
           \s""";

    private String result = """
            Лобовое сопротивление: %s Н
            Момент относительно оси х: %s Н·м
            Коэффициент лобового сопротивления: %s
            Подъемная сила: %s Н
            Момент относительно оси y: %s Н·м
            Коэффициент подъемной силы: %s
            Скоростной напор: %s Па
            """;

/*    @FXML
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

        String forceX = String.valueOf(calculationService.calculateForceX(heightKm, radius, lengthNtu, formName, speed));
        String momentX = String.valueOf(calculationService.calculateMomentX(heightKm, radius, lengthNtu, speed, formName, cubesatSize, characteristicsNtu));
        String coefficientX = String.valueOf(calculationService.calculateCoefficientX(radius, lengthNtu, formName));

        String forceY = String.valueOf(calculationService.calculateForceY(heightKm, radius, lengthNtu, alfa, formName, speed));
        String momentY = String.valueOf(calculationService.calculateMomentY(heightKm, radius, alfa, lengthNtu, speed, formName, cubesatSize, characteristicsNtu));
        String coefficientY = String.valueOf(calculationService.calculateCoefficientY(alfa));

        String velocityHead = String.valueOf(calculationService.calculateVelocityHead(heightKm, speed));

        resultTextArea.setText(
                String.format(result, forceX, momentX, coefficientX, forceY, momentY, coefficientY, velocityHead)
        );


    }



    private String result = """
            Лобовое сопративление: %s \n
            Кренящий момент: %s \n
            Коэффициент лобового сопративления: %s \n
            Подъемная сила: %s \n
            Тангажный момент: %s \n
            Коэффициент подъемной силы: %s \n
            Скоростной напор: %s \n
             
            """;*/
}
