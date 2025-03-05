package org.example.demo.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.FlightCharacteristics;
import org.example.demo.dto.FlightCharacteristicsForTime;
import org.example.demo.dto.NtuTableDto;
import org.example.demo.entity.AerodynamicCharacteristics;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.math.ReentrySimulation;
import org.example.demo.repository.AerodynamicCharacteristicsRepository;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.CubesatSizeRepository;
import org.example.demo.repository.FormNtuRepository;
import org.example.demo.repository.MaterialInfoRepository;
import org.example.demo.service.MainCalculationService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class Controller {

    private final CubesatSizeRepository cubesatSizeRepository;
    private final MaterialInfoRepository materialInfoRepository;
    private final FormNtuRepository formNtuRepository;
    private final CharacteristicsNtuRepository characteristicsNtuRepository;
    private final MainCalculationService calculationService;
    private final AerodynamicCharacteristicsRepository aerodynamicCharacteristicsRepository;
    private final ReentrySimulation reentrySimulation;

    private CubesatSize actualCubesatSize;
    private CharacteristicsNtu actualCharacteristicsNtu;
    private double actualHeightKm;
    private double actualAlfa;
    private double actualSpeed;
    private FlightCharacteristics actualFlightChar;
    private FlightCharacteristicsForTime actualFlightCharForTime;

    private double actualAlfaTime;
    private double actualSpeedTime;
    private double theta0Time;
    private double startHeightTime;
    private double endHeightTime;

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
    private Button chooseFlight, chooseTimeFlight;

    @FXML
    private Button addCubesat, addNtu, addMaterial;

    @FXML
    private Button calculateButton, calculateTimeButton;

    @FXML
    private Button deleteCubesatButton, deleteNtuButton, deleteMaterialButton;


    @FXML
    private TextField heightFlight, speedFlight, angleFlight;

    @FXML
    private TextField startHeightField, endHeightField, theta0Field, speedTimeField, angleTimeField;

    // условно логи
    @FXML
    private Label flightLog, cubesatLog, ntuLog, materialLog;

    @FXML
    private Label chosenCubesat, chosenNtu;

    // информация с характеристиками полета
    @FXML
    private TextArea flightArea, flightTimeArea;

    @FXML
    private TextArea resultTextArea, timeResultTextArea;

    @FXML
    private ImageView firstImage, secondImage;


    @FXML
    public void initialize() {
        setupTableColumns();
        loadTableData();
        Image image = new Image("pictures/schema.png"); // укажите правильный путь к изображению
        firstImage.setImage(image);

        cubesatTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                double length = newSelection.getLength();
                double width = newSelection.getWidth();
                double height = newSelection.getHeight();
                chosenCubesat.setText(String.format(cubesatChosen, length, width, height));
            } else {
                chosenCubesat.setText(null);
            }
        });

        ntuTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                String fileName = formNtuRepository.findByName(newSelection.getForm()).get().getFileName();
                double radius = newSelection.getRadius();
                double length = newSelection.getLength();
                if (length == 0.0) {
                    chosenNtu.setText(String.format(ntuChosenWithoutLength, radius));
                } else {
                    chosenNtu.setText(String.format(ntuChosen, length, radius));
                }
                System.out.println(fileName);
                String url = "pictures/" + fileName + ".png";
                System.out.println(url);
                Image imageForm = new Image(url);
                secondImage.setImage(imageForm);
            } else {
                secondImage.setImage(null); // Сбросить изображение, если ничего не выбрано
                chosenNtu.setText(null);
            }
        });

    }

    private final String cubesatChosen = "Длина - %s м, Ширина - %s м, Высота - %s м";
    private final String ntuChosen = "Длина - %s м, Радиус - %s м";
    private final String ntuChosenWithoutLength = "Радиус - %s м";

    @FXML
    public void saveNewCubesat() throws IOException {
        try {
            // Загружаем FXML-файл для нового окна
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-cubesat.fxml"));
            loader.setControllerFactory(param -> new NewCubesatController(cubesatSizeRepository));
            Parent root = loader.load();

            // Получаем контроллер для нового окна
            NewCubesatController newCubesatController = loader.getController();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Новый Cubesat");
            stage.setScene(new Scene(root));
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon); // Устанавливаем иконку для окна
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            // Передаем Stage в контроллер
            newCubesatController.setStage(stage);

            stage.setOnHidden(event -> {
                List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
                cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));
            });

            // Отображаем окно
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            cubesatLog.setText("Не удалось добавить по неизвестной причине");
        }
    }

    @FXML
    public void deleteCubesatSize() {
        try {
            String cubesatName = cubesatTableView.getSelectionModel().getSelectedItem().getName();
            CubesatSize cubesatSize = cubesatSizeRepository.findByName(cubesatName).get();

            List<AerodynamicCharacteristics> listAeroChar = aerodynamicCharacteristicsRepository.findAllByCubesatSizeId(cubesatSize.getId());
            aerodynamicCharacteristicsRepository.deleteAll(listAeroChar);

            cubesatSizeRepository.delete(cubesatSize);
            cubesatLog.setText("Запись с name " + cubesatName + " успешно удалена"); //

            List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
            cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));

        } catch (Exception e) {
            cubesatLog.setText("Запись " + cubesatTableView.getSelectionModel().getSelectedItem().getName() + " не удалена по неизвестной причине");
        }
    }

    @FXML
    public void saveNewNtu() {
        try {
            // Загружаем FXML-файл для нового окна
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-ntu.fxml"));
            loader.setControllerFactory(param -> new NewNtuController(characteristicsNtuRepository, formNtuRepository,
                    materialInfoRepository));
            Parent root = loader.load();

            // Получаем контроллер для нового окна
            NewNtuController newNtuController = loader.getController();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Новая оболочка");
            stage.setScene(new Scene(root));
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon); // Устанавливаем иконку для окна
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            // Передаем Stage в контроллер
            newNtuController.setStage(stage);

            stage.setOnHidden(event -> {
                List<NtuTableDto> ntuData = getAllForTable();
                ntuTableView.setItems(FXCollections.observableArrayList(ntuData));
            });

            // Отображаем окно
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            ntuLog.setText("Не удалось добавить по неизвестной причине");
        }
    }

    @FXML
    private void deleteNtu() {
        try {
            String ntuName = ntuTableView.getSelectionModel().getSelectedItem().getName();
            CharacteristicsNtu ntu = characteristicsNtuRepository.findByName(ntuName).get();

            List<AerodynamicCharacteristics> listAeroChar = aerodynamicCharacteristicsRepository.findAllByNtuId(ntu.getId());
            aerodynamicCharacteristicsRepository.deleteAll(listAeroChar);
            characteristicsNtuRepository.delete(ntu);

            ntuLog.setText("Запись с name " + ntuName + " успешно удалена");

            List<NtuTableDto> ntuData = getAllForTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        } catch (Exception e) {
            ntuLog.setText("Запись " + ntuTableView.getSelectionModel().getSelectedItem().getName() + "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void saveNewMaterial() {
        try {
            // Загружаем FXML-файл для нового окна
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-material.fxml"));
            loader.setControllerFactory(param -> new NewMaterialController(materialInfoRepository));
            Parent root = loader.load();

            // Получаем контроллер для нового окна
            NewMaterialController newMaterialController = loader.getController();

            // Создаем новое окно (Stage)
            Stage stage = new Stage();
            stage.setTitle("Новый материал");
            stage.setScene(new Scene(root));

            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon); // Устанавливаем иконку для окна
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            // Передаем Stage в контроллер
            newMaterialController.setStage(stage);

            stage.setOnHidden(event -> {
                List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
                materialTableView.setItems(FXCollections.observableArrayList(materialData));
            });

            // Отображаем окно
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            materialLog.setText("Не удалось добавить по неизвестной причине");
        }
    }

    @FXML
    private void deleteMaterial() {
        try {
            String materialName = materialTableView.getSelectionModel().getSelectedItem().getName();
            MaterialInfoEntity material = materialInfoRepository.findByName(materialName).get();
            List<CharacteristicsNtu> ntus = characteristicsNtuRepository.findAllByMaterial(material);
            List<Integer> ntuIds = ntus.stream()
                    .map(CharacteristicsNtu::getId)
                    .toList();
            List<AerodynamicCharacteristics> listAeroChar = aerodynamicCharacteristicsRepository.findAllByNtuIdIn(ntuIds);
            aerodynamicCharacteristicsRepository.deleteAll(listAeroChar);
            characteristicsNtuRepository.deleteAll(ntus);
            materialInfoRepository.delete(material);
            materialLog.setText("Запись с name " + materialName + " успешно удалена");


            List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
            materialTableView.setItems(FXCollections.observableArrayList(materialData));

            List<NtuTableDto> ntuData = getAllForTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        } catch (Exception e) {
            materialLog.setText("Запись " + materialTableView.getSelectionModel().getSelectedItem().getName()
                    + "не удалена по неизвестной причине");
        }
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
            flightLog.setText("Некорректный формат данных");
        }
    }

    @FXML
    private void chooseFlightCharForTime() {
        String startHeight = startHeightField.getText();
        String endHeight = endHeightField.getText();
        String speed = speedTimeField.getText();
        String alfa = angleTimeField.getText();
        String theta0 = theta0Field.getText();
        if (startHeight.isBlank() || endHeight.isBlank() || theta0.isBlank() || alfa.isBlank() || speed.isBlank()) {
            flightLog.setText("Заполнены не все поля");
            return;
        }
        try {
            startHeightTime = convertStringToDouble(startHeight);
            endHeightTime = convertStringToDouble(endHeight);
            actualAlfaTime = convertStringToDoubleWithMinus(alfa);
            theta0Time = convertStringToDoubleWithMinus(alfa);
            actualSpeedTime = convertStringToDouble(speed);

            FlightCharacteristicsForTime flightCharacteristics = new FlightCharacteristicsForTime();
            flightCharacteristics.setStartHeight(startHeightTime);
            flightCharacteristics.setEndHeight(endHeightTime);
            flightCharacteristics.setAlfa(actualAlfaTime);
            flightCharacteristics.setTheta0(theta0Time);
            flightCharacteristics.setSpeed(actualSpeedTime);
            actualFlightCharForTime = flightCharacteristics;
            flightTimeArea.setText(String.format(flightTimeChar, startHeightTime, endHeightTime, actualSpeedTime, actualAlfaTime, theta0Time));
        } catch (NumberFormatException e) {
            flightLog.setText("Некорректный формат данных");
        }

    }


    @FXML
    private void calculate() {
        try {
            String cubesatName = cubesatTableView.getSelectionModel().getSelectedItem().getName();
            actualCubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
            String charNtuName = ntuTableView.getSelectionModel().getSelectedItem().getName();
            actualCharacteristicsNtu = characteristicsNtuRepository.findByName(charNtuName).get();
        } catch (NullPointerException e) {
            resultTextArea.setText("Не все поля заполнены");
            return;
        }
        if (actualCharacteristicsNtu == null || actualCubesatSize == null || actualFlightChar == null) {
            resultTextArea.setText("Не все поля заполнены");
        } else {
            double alfa = actualAlfa * (Math.PI / 180); // тут радианы
            double speed = actualSpeed; // тут метры в секунду
            double radius = actualCharacteristicsNtu.getRadius();
            double length = actualCharacteristicsNtu.getLength();
            String formName = actualCharacteristicsNtu.getForm().getFileName();

            double forceX = calculationService.calculateForceX(actualHeightKm, radius, length, alfa, formName, speed);
            double forceY = calculationService.calculateForceY(actualHeightKm, radius, length, alfa, formName, speed);
            double momentX = calculationService.calculateMomentX(actualHeightKm, radius, length, alfa, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double momentY = calculationService.calculateMomentY(actualHeightKm, radius, alfa, length, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double coefficientX = calculationService.calculateCoefficientX(radius, length, formName, alfa);
            double coefficientY = calculationService.calculateCoefficientY(alfa);
            double velocityHead = calculationService.calculateVelocityHead(actualHeightKm, speed);
            double density = calculationService.getDensity(actualHeightKm);
            double minSpeed = calculationService.calculateMinSpeed(actualHeightKm);
            System.out.println("Плотность " + density);

            String strForceX = String.valueOf(forceX);
            String strForceY = String.valueOf(forceY);
            String strMomentX = String.valueOf(momentX);
            String strMomentY = String.valueOf(momentY);
            String strCoefficientX = String.valueOf(coefficientX);
            String strCoefficientY = String.valueOf(coefficientY);
            String strVelocityHead = String.valueOf(velocityHead);

            resultTextArea.setText(
                    String.format(result, strForceX, strMomentX, strCoefficientX, strForceY, strMomentY, strCoefficientY, strVelocityHead)
            );

            saveAeroCharInDb(alfa, forceX, momentX, forceY, momentY, coefficientX, coefficientY, velocityHead, density, speed, minSpeed);
        }
    }

    @FXML
    private void calculateTime() {
        try {
            String cubesatName = cubesatTableView.getSelectionModel().getSelectedItem().getName();
            actualCubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
            String charNtuName = ntuTableView.getSelectionModel().getSelectedItem().getName();
            actualCharacteristicsNtu = characteristicsNtuRepository.findByName(charNtuName).get();
        } catch (NullPointerException e) {
            timeResultTextArea.setText("Не все поля заполнены");
            return;
        }
        if (actualCharacteristicsNtu == null || actualCubesatSize == null || actualFlightCharForTime == null) {
            timeResultTextArea.setText("Не все поля заполнены");
        } else {
            double radius = actualCharacteristicsNtu.getRadius();
            double length = actualCharacteristicsNtu.getLength();
            String formName = actualCharacteristicsNtu.getForm().getFileName();

            double startHeight = startHeightTime * 1000;
            double endHeight = endHeightTime * 1000;
            double speed = actualSpeedTime;
            double theta0 = theta0Time * (Math.PI / 180);
            double alfa = actualAlfaTime * (Math.PI / 180);

            double forceX = calculationService.calculateForceX(actualHeightKm, radius, length, alfa, formName, speed);
            double forceY = calculationService.calculateForceY(actualHeightKm, radius, length, alfa, formName, speed);
            double mass = calculationService.calculateFullMass(actualCubesatSize, actualCharacteristicsNtu, alfa);

            //double landTime = reentrySimulation.calculateTime(startHeight, endHeight, speed, theta0, forceX, forceY, mass);
            double landTime = 15;

            String strStartHeight = String.valueOf(startHeight);
            String strEndHeight = String.valueOf(endHeight);
            String strTheta0 = String.valueOf(theta0);
            String strLandTime = String.valueOf(landTime);

            timeResultTextArea.setText(String.format(resultTime, strStartHeight, strEndHeight, strTheta0, strLandTime));
        }

    }

    private void saveAeroCharInDb(double alfa, double forceX, double momentX, double forceY, double momentY, double coefficientX, double coefficientY, double velocityHead, double density, double speed, double minSpeed) {
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
        aeroChar.setHeightKm(actualHeightKm);
        aerodynamicCharacteristicsRepository.save(aeroChar);
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

    private String flightTimeChar = """
             Начальная орбита: %s  км
                            \s
             Конечная орбита: %s  км
                            \s
             Скорость: %s м/с
                            \s
             Угол атаки: %s  градусов
                            \s
             Угол наклона траектории: %s  градусов
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

    private String resultTime = """
            Время спуска с орбиты %s
            на орбиту %s
            при начальном угле наклона траектории %s
            составило t = %s секунд
            """;
}
