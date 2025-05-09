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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import org.example.demo.dto.AerodynamicCharacteristicsDto;
import org.example.demo.dto.NtuTableDto;
import org.example.demo.dto.TimeCharacteristicsDto;
import org.example.demo.entity.AerodynamicCharacteristics;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.entity.OrbitCharacteristics;
import org.example.demo.entity.OrbitCharacteristicsTime;
import org.example.demo.entity.TimeCharacteristics;
import org.example.demo.math.OrbitalDescentCalculator;
import org.example.demo.repository.AerodynamicCharacteristicsRepository;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.CubesatSizeRepository;
import org.example.demo.repository.FormNtuRepository;
import org.example.demo.repository.MaterialInfoRepository;
import org.example.demo.repository.OrbitCharacteristicsRepository;
import org.example.demo.repository.OrbitCharacteristicsTimeRepository;
import org.example.demo.repository.TimeCharacteristicsRepository;
import org.example.demo.service.AreaCalculator;
import org.example.demo.service.MainCalculationService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.example.demo.util.Constant.ACCELERATION_OF_GRAVITY;
import static org.example.demo.util.Constant.EARTH_RADIUS;
import static org.example.demo.util.Constant.RADIUS_EARTH;
import static org.example.demo.util.Constant.VELOCITY_OF_EARTH;

@Component
@RequiredArgsConstructor
public class Controller {

    private final CubesatSizeRepository cubesatSizeRepository;
    private final MaterialInfoRepository materialInfoRepository;
    private final FormNtuRepository formNtuRepository;
    private final CharacteristicsNtuRepository characteristicsNtuRepository;
    private final MainCalculationService calculationService;
    private final AerodynamicCharacteristicsRepository aeroCharRepository;
    private final AreaCalculator areaCalculator;
    private final OrbitCharacteristicsRepository orbitCharacteristicsRepository;
    private final OrbitCharacteristicsTimeRepository orbitCharacteristicsTimeRepository;
    private final TimeCharacteristicsRepository timeCharacteristicsRepository;

    private CubesatSize actualCubesatSize;
    private CharacteristicsNtu actualCharacteristicsNtu;

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
    private TableView<OrbitCharacteristics> orbitaTableView;

    @FXML
    private TableColumn<OrbitCharacteristics, Double> orbitColumn, speedColumn, alfaColumn;

    @FXML
    private TableView<OrbitCharacteristicsTime> orbitaTimeTableView;

    @FXML
    private TableColumn<OrbitCharacteristicsTime, Integer> calculationIdChColumn;

    @FXML
    private TableColumn<OrbitCharacteristicsTime, Double> initialOrbitColumn, finalOrbitColumn, speedTimeColumn, alfaTimeColumn, gammaTimeColumn;

    @FXML
    private TableView<MaterialInfoEntity> materialTableView;

    @FXML
    private TableColumn<MaterialInfoEntity, String> nameMaterialColumn;

    @FXML
    private TableColumn<MaterialInfoEntity, Double> densityMaterialColumn;

    @FXML
    private TableView<AerodynamicCharacteristicsDto> aeroCharTableView;

    @FXML
    private TableColumn<AerodynamicCharacteristicsDto, String> cubesatColumn, ntuColumn;

    @FXML
    private TableColumn<AerodynamicCharacteristicsDto, Double> orbitChColumn, speedChColumn, alfaChColumn, forceXColumn;

    @FXML
    private TableColumn<AerodynamicCharacteristicsDto, Double> coefficientXColumn, momentXColumn, forceYColumn;

    @FXML
    private TableColumn<AerodynamicCharacteristicsDto, Double> coefficientYColumn, momentYColumn, velocityHeadColumn;

    @FXML
    private TableView<TimeCharacteristicsDto> timeCharTableView;

    @FXML
    private TableColumn<TimeCharacteristicsDto, Integer> calculationIdColumn;

    @FXML
    private TableColumn<TimeCharacteristicsDto, String> cubesatTimeColumn, ntuTimeColumn;

    @FXML
    private TableColumn<TimeCharacteristicsDto, Double> initOrbitColumn, finOrbitColumn, speedTimeChColumn, alfaTimeChColumn, gammaTimeChColumn, landTimeColumn;

    @FXML
    private Button addCubesat, addNtu, addMaterial, addOrbitButton, addOrbitTimeButton;

    @FXML
    private Button calculateButton, calculateTimeButton;

    @FXML
    private Button deleteCubesatButton, deleteNtuButton, deleteMaterialButton, deleteOrbitButton, deleteOrbitTimeButton;

    @FXML
    private Button deleteAeroButton, deleteTimeButton;

    // условно логи
    @FXML
    private Label cubesatLog, ntuLog, materialLog, orbitLog, orbitTimeLog;

    @FXML
    private Label chosenCubesat, chosenNtu, chosenOrbit, chosenOrbitTime;

    @FXML
    private Label chosenAeroCharLog, chosenTimeCharLog;

    @FXML
    private TextArea resultTextArea, timeResultTextArea;

    @FXML
    private ImageView firstImage, secondImage;


    @FXML
    public void initialize() {
        setupTableColumns();
        loadTableData();
        Image image = new Image("pictures/schema.png");
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
                secondImage.setImage(null);
                chosenNtu.setText(null);
            }
        });
        orbitaTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                double orbit = newSelection.getOrbit();
                double speed = newSelection.getSpeed();
                double alfa = newSelection.getAlfa();
                chosenOrbit.setText(String.format(orbitChosen, orbit, speed, alfa));
            } else {
                chosenOrbit.setText(null);
            }
        });
        orbitaTimeTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                double initOrbit = newSelection.getInitialOrbit();
                double finalOrbit = newSelection.getFinalOrbit();
                double speed = newSelection.getSpeed();
                double alfa = newSelection.getAlfa();
                double gamma = newSelection.getGamma();
                chosenOrbitTime.setText(String.format(orbitTimeChosen, initOrbit, finalOrbit, speed, alfa, gamma));
            } else {
                chosenOrbitTime.setText(null);
            }
        });
    }

    @FXML
    public void saveNewCubesat() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-cubesat.fxml"));
            loader.setControllerFactory(param -> new NewCubesatController(cubesatSizeRepository));
            Parent root = loader.load();

            NewCubesatController newCubesatController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Новый Cubesat");
            stage.setScene(new Scene(root));
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            newCubesatController.setStage(stage);

            stage.setOnHidden(event -> {
                List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
                cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));
            });

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

            List<AerodynamicCharacteristics> listAeroChar = aeroCharRepository.findAllByCubesatSizeId(cubesatSize.getId());
            aeroCharRepository.deleteAll(listAeroChar);

            cubesatSizeRepository.delete(cubesatSize);
            cubesatLog.setText("Запись с name " + cubesatName + " успешно удалена");

            List<CubesatSize> cubesatData = cubesatSizeRepository.findAll();
            cubesatTableView.setItems(FXCollections.observableArrayList(cubesatData));

        } catch (Exception e) {
            cubesatLog.setText("Запись " + cubesatTableView.getSelectionModel().getSelectedItem().getName() + " не удалена по неизвестной причине");
        }
    }

    @FXML
    public void saveNewNtu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-ntu.fxml"));
            loader.setControllerFactory(param -> new NewNtuController(characteristicsNtuRepository, formNtuRepository,
                    materialInfoRepository));
            Parent root = loader.load();

            NewNtuController newNtuController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Новая оболочка");
            stage.setScene(new Scene(root));
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            newNtuController.setStage(stage);

            stage.setOnHidden(event -> {
                List<NtuTableDto> ntuData = getAllForNtuTable();
                ntuTableView.setItems(FXCollections.observableArrayList(ntuData));
            });

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

            List<AerodynamicCharacteristics> listAeroChar = aeroCharRepository.findAllByNtuId(ntu.getId());
            aeroCharRepository.deleteAll(listAeroChar);
            characteristicsNtuRepository.delete(ntu);

            ntuLog.setText("Запись с name " + ntuName + " успешно удалена");

            List<NtuTableDto> ntuData = getAllForNtuTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        } catch (Exception e) {
            ntuLog.setText("Запись " + ntuTableView.getSelectionModel().getSelectedItem().getName() + "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void saveOrbit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-orbit.fxml"));
            loader.setControllerFactory(param -> new NewOrbitController(orbitCharacteristicsRepository));
            Parent root = loader.load();

            NewOrbitController newOrbitController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Добавить новые данные");
            stage.setScene(new Scene(root));

            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }
            newOrbitController.setStage(stage);

            stage.setOnHidden(event -> {
                List<OrbitCharacteristics> orbitCharacteristics = orbitCharacteristicsRepository.findAll();
                orbitaTableView.setItems(FXCollections.observableArrayList(orbitCharacteristics));
            });
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            orbitLog.setText("Не удалось добавить по неизвестной причине");
        }
    }

    @FXML
    private void deleteOrbit() {
        try {
            double orbit = orbitaTableView.getSelectionModel().getSelectedItem().getOrbit();
            double speed = orbitaTableView.getSelectionModel().getSelectedItem().getSpeed();
            double alfa = orbitaTableView.getSelectionModel().getSelectedItem().getAlfa();
            List<OrbitCharacteristics> orbitCharacteristics =
                    orbitCharacteristicsRepository.findAllByOrbitAndSpeedAndAlfa(orbit, speed, alfa);
            orbitCharacteristicsRepository.deleteAll(orbitCharacteristics);
            orbitLog.setText("Успешно удалено");

            List<OrbitCharacteristics> orbitChar = orbitCharacteristicsRepository.findAll();
            orbitaTableView.setItems(FXCollections.observableArrayList(orbitChar));

        } catch (Exception e) {
            orbitLog.setText("Запись не удалена по неизвестной причине");
        }
    }

    @FXML
    private void saveOrbitTime() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-orbit-time.fxml"));
            loader.setControllerFactory(param -> new NewOrbitTimeController(orbitCharacteristicsTimeRepository));
            Parent root = loader.load();

            NewOrbitTimeController newOrbitTimeController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Добавить новые данные");
            stage.setScene(new Scene(root));

            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }
            newOrbitTimeController.setStage(stage);

            stage.setOnHidden(event -> {
                List<OrbitCharacteristicsTime> orbitCharacteristics = orbitCharacteristicsTimeRepository.findAll();
                orbitaTimeTableView.setItems(FXCollections.observableArrayList(orbitCharacteristics));
            });
            stage.show();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            orbitTimeLog.setText("Не удалось добавить по неизвестной причине");
        }
    }

    @FXML
    private void deleteOrbitTime() {
        try {
            double initOrbit = orbitaTimeTableView.getSelectionModel().getSelectedItem().getInitialOrbit();
            double finalOrbit = orbitaTimeTableView.getSelectionModel().getSelectedItem().getFinalOrbit();
            double speed = orbitaTimeTableView.getSelectionModel().getSelectedItem().getSpeed();
            double alfa = orbitaTimeTableView.getSelectionModel().getSelectedItem().getAlfa();
            double gamma = orbitaTimeTableView.getSelectionModel().getSelectedItem().getGamma();
            List<OrbitCharacteristicsTime> orbitCharacteristics =
                    orbitCharacteristicsTimeRepository.findAllByInitialOrbitAndFinalOrbitAndSpeedAndAlfaAndGamma(
                            initOrbit, finalOrbit, speed, alfa, gamma
                    );
            orbitCharacteristicsTimeRepository.deleteAll(orbitCharacteristics);
            orbitTimeLog.setText("Успешно удалено");

            List<OrbitCharacteristicsTime> orbitChar = orbitCharacteristicsTimeRepository.findAll();
            orbitaTimeTableView.setItems(FXCollections.observableArrayList(orbitChar));

        } catch (Exception e) {
            orbitTimeLog.setText("Запись не удалена по неизвестной причине");
        }
    }

    @FXML
    private void saveNewMaterial() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/save-material.fxml"));
            loader.setControllerFactory(param -> new NewMaterialController(materialInfoRepository));
            Parent root = loader.load();

            NewMaterialController newMaterialController = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Новый материал");
            stage.setScene(new Scene(root));

            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/icons/icon.png")));
                stage.getIcons().add(icon);
            } catch (Exception e) {
                System.err.println("Ошибка загрузки иконки: " + e.getMessage());
            }

            newMaterialController.setStage(stage);

            stage.setOnHidden(event -> {
                List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
                materialTableView.setItems(FXCollections.observableArrayList(materialData));
            });

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
            List<AerodynamicCharacteristics> listAeroChar = aeroCharRepository.findAllByNtuIdIn(ntuIds);
            aeroCharRepository.deleteAll(listAeroChar);
            characteristicsNtuRepository.deleteAll(ntus);
            materialInfoRepository.delete(material);
            materialLog.setText("Запись с name " + materialName + " успешно удалена");


            List<MaterialInfoEntity> materialData = materialInfoRepository.findAll();
            materialTableView.setItems(FXCollections.observableArrayList(materialData));

            List<NtuTableDto> ntuData = getAllForNtuTable();
            ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        } catch (Exception e) {
            materialLog.setText("Запись " + materialTableView.getSelectionModel().getSelectedItem().getName()
                                + "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void calculate() {
        OrbitCharacteristics orbitChar;
        try {
            String cubesatName = cubesatTableView.getSelectionModel().getSelectedItem().getName();
            actualCubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
            String charNtuName = ntuTableView.getSelectionModel().getSelectedItem().getName();
            actualCharacteristicsNtu = characteristicsNtuRepository.findByName(charNtuName).get();
            double orbit = orbitaTableView.getSelectionModel().getSelectedItem().getOrbit();
            double speed = orbitaTableView.getSelectionModel().getSelectedItem().getSpeed();
            double alfa = orbitaTableView.getSelectionModel().getSelectedItem().getAlfa();
            orbitChar = orbitCharacteristicsRepository.findAllByOrbitAndSpeedAndAlfa(orbit, speed, alfa).stream().findFirst().get();

        } catch (NullPointerException e) {
            resultTextArea.setText("Не все поля заполнены");
            return;
        }
        if (actualCharacteristicsNtu == null || actualCubesatSize == null || orbitChar == null) {
            resultTextArea.setText("Не все поля заполнены");
        } else {
            double alfa = orbitChar.getAlfa() * (Math.PI / 180); // тут радианы
            double speed = orbitChar.getSpeed(); // тут метры в секунду
            double height = orbitChar.getOrbit();
            double radius = actualCharacteristicsNtu.getRadius();
            double length = actualCharacteristicsNtu.getLength();
            String formName = actualCharacteristicsNtu.getForm().getFileName();

            double forceX = calculationService.calculateForceX(height, radius, length, alfa, formName, speed);
            double forceY = calculationService.calculateForceY(height, radius, length, alfa, formName, speed);
            double momentX = calculationService.calculateMomentX(height, radius, length, alfa, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double momentY = calculationService.calculateMomentY(height, radius, alfa, length, speed, formName,
                    actualCubesatSize, actualCharacteristicsNtu);
            double coefficientX = calculationService.calculateCoefficientX(radius, length, formName, alfa);
            double coefficientY = calculationService.calculateCoefficientY(alfa);
            double velocityHead = calculationService.calculateVelocityHead(height, speed);
            double density = calculationService.getDensity(height);
            System.out.println("density = " + density);
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

            saveAeroCharInDb(orbitChar.getId(), forceX, momentX, forceY, momentY, coefficientX, coefficientY, velocityHead);

            List<AerodynamicCharacteristicsDto> aeroCharNtu = getAllForAeroCharTable();
            aeroCharTableView.setItems(FXCollections.observableArrayList(aeroCharNtu));
        }
    }

    @FXML
    private void calculateTime() {
        OrbitCharacteristicsTime orbitChar;
        try {
            String cubesatName = cubesatTableView.getSelectionModel().getSelectedItem().getName();
            actualCubesatSize = cubesatSizeRepository.findByName(cubesatName).get();
            String charNtuName = ntuTableView.getSelectionModel().getSelectedItem().getName();
            actualCharacteristicsNtu = characteristicsNtuRepository.findByName(charNtuName).get();
            double initialOrbit = orbitaTimeTableView.getSelectionModel().getSelectedItem().getInitialOrbit();
            double finalOrbit = orbitaTimeTableView.getSelectionModel().getSelectedItem().getFinalOrbit();
            double speed = orbitaTimeTableView.getSelectionModel().getSelectedItem().getSpeed();
            double alfa = orbitaTimeTableView.getSelectionModel().getSelectedItem().getAlfa();
            double gamma = orbitaTimeTableView.getSelectionModel().getSelectedItem().getGamma();
            orbitChar = orbitCharacteristicsTimeRepository.findAllByInitialOrbitAndFinalOrbitAndSpeedAndAlfaAndGamma(initialOrbit, finalOrbit, speed, alfa, gamma).stream().findFirst().get();

        } catch (NullPointerException e) {
            timeResultTextArea.setText("Не все поля заполнены");
            return;
        }
        if (actualCharacteristicsNtu == null || actualCubesatSize == null || orbitChar == null) {
            timeResultTextArea.setText("Не все поля заполнены");
        } else {
            double radius = actualCharacteristicsNtu.getRadius();
            double length = actualCharacteristicsNtu.getLength();
            String formName = actualCharacteristicsNtu.getForm().getFileName();

            double startHeight = orbitChar.getInitialOrbit() * 1000 + EARTH_RADIUS;
            double endHeight = orbitChar.getFinalOrbit() * 1000 + EARTH_RADIUS;
            double speed = orbitChar.getSpeed();
            double theta0 = orbitChar.getGamma() * (Math.PI / 180);
            double alfa = orbitChar.getAlfa() * (Math.PI / 180);
            double g_r = ACCELERATION_OF_GRAVITY;
            double Omega = VELOCITY_OF_EARTH;

            double mass = calculationService.calculateFullMass(actualCubesatSize, actualCharacteristicsNtu, alfa);

            double coefficientX = calculationService.calculateCoefficientX(radius, length, formName, alfa);
            coefficientX = Math.max(coefficientX, 1e-6);
            double coefficientY = calculationService.calculateCoefficientY(alfa);
            coefficientY = Math.max(coefficientY, 1e-6);

            double area = areaCalculator.calculateArea(formName, radius, length, alfa);

            OrbitalDescentCalculator calculator = new OrbitalDescentCalculator(startHeight, endHeight, speed, theta0, mass, g_r, Omega, coefficientX, coefficientY, area);

            double landTime = calculator.calculateDescentTime();

            String strStartHeight = String.valueOf((startHeight - EARTH_RADIUS) / 1000);
            String strEndHeight = String.valueOf((endHeight - RADIUS_EARTH) / 1000);
            String strLandTime = String.valueOf(landTime);

            timeResultTextArea.setText(String.format(resultTime, strStartHeight, strEndHeight, strLandTime));
            saveTimeCharInDb(orbitChar.getId(), landTime);

            List<TimeCharacteristicsDto> timeCharDto = getAllForTimeCharTable();
            timeCharTableView.setItems(FXCollections.observableArrayList(timeCharDto));
        }
    }

    @FXML
    private void deleteAeroChar() {
        try {
            int id = aeroCharTableView.getSelectionModel().getSelectedItem().getId();
            AerodynamicCharacteristics aero = aeroCharRepository.findById(id).get();
            aeroCharRepository.delete(aero);
            chosenAeroCharLog.setText("Запись с номером " + id + " успешно удалена");

            List<AerodynamicCharacteristicsDto> aeroCharNtu = getAllForAeroCharTable();
            aeroCharTableView.setItems(FXCollections.observableArrayList(aeroCharNtu));
        } catch (Exception e) {
            chosenAeroCharLog.setText("Запись c номером " + aeroCharTableView.getSelectionModel().getSelectedItem().getId() +
                                      "не удалена по неизвестной причине");
        }
    }

    @FXML
    private void deleteTimeChar() {
        try {
            int id = timeCharTableView.getSelectionModel().getSelectedItem().getId();
            TimeCharacteristics time = timeCharacteristicsRepository.findById(id).get();
            timeCharacteristicsRepository.delete(time);
            chosenTimeCharLog.setText("Запись с номером " + id + " успешно удалена");

            List<TimeCharacteristicsDto> timeCharDto = getAllForTimeCharTable();
            timeCharTableView.setItems(FXCollections.observableArrayList(timeCharDto));
        } catch (Exception e) {
            chosenTimeCharLog.setText("Запись c номером " + timeCharTableView.getSelectionModel().getSelectedItem().getId() +
                                      "не удалена по неизвестной причине");
        }
    }

    private void saveAeroCharInDb(int orbitId, double forceX, double momentX, double forceY, double momentY, double coefficientX, double coefficientY, double velocityHead) {
        AerodynamicCharacteristics aeroChar = new AerodynamicCharacteristics();
        aeroChar.setCubesatSizeId(actualCubesatSize.getId());
        aeroChar.setNtuId(actualCharacteristicsNtu.getId());
        aeroChar.setOrbitId(orbitId);
        aeroChar.setForceX(forceX);
        aeroChar.setMomentX(momentX);
        aeroChar.setForceY(forceY);
        aeroChar.setMomentY(momentY);
        aeroChar.setCoefficientX(coefficientX);
        aeroChar.setCoefficientY(coefficientY);
        aeroChar.setVelocityHead(velocityHead);
        aeroCharRepository.save(aeroChar);
    }

    private void saveTimeCharInDb(int orbitTimeId, double landTime) {
        TimeCharacteristics timeChar = new TimeCharacteristics();
        timeChar.setCubesatSizeId(actualCubesatSize.getId());
        timeChar.setNtuId(actualCharacteristicsNtu.getId());
        timeChar.setOrbitIdTime(orbitTimeId);
        timeChar.setLandTime(landTime);
        timeCharacteristicsRepository.save(timeChar);
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

        List<OrbitCharacteristics> orbitCharacteristics = orbitCharacteristicsRepository.findAll();
        orbitaTableView.setItems(FXCollections.observableArrayList(orbitCharacteristics));

        List<OrbitCharacteristicsTime> orbitCharacteristicsTime = orbitCharacteristicsTimeRepository.findAll();
        orbitaTimeTableView.setItems(FXCollections.observableArrayList(orbitCharacteristicsTime));

        List<NtuTableDto> ntuData = getAllForNtuTable();
        ntuTableView.setItems(FXCollections.observableArrayList(ntuData));

        List<AerodynamicCharacteristicsDto> aeroCharNtu = getAllForAeroCharTable();
        aeroCharTableView.setItems(FXCollections.observableArrayList(aeroCharNtu));

        List<TimeCharacteristicsDto> timeCharDto = getAllForTimeCharTable();
        timeCharTableView.setItems(FXCollections.observableArrayList(timeCharDto));
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

        orbitColumn.setCellValueFactory(new PropertyValueFactory<>("orbit"));
        speedColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        alfaColumn.setCellValueFactory(new PropertyValueFactory<>("alfa"));

        initialOrbitColumn.setCellValueFactory(new PropertyValueFactory<>("initialOrbit"));
        finalOrbitColumn.setCellValueFactory(new PropertyValueFactory<>("finalOrbit"));
        speedTimeColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        alfaTimeColumn.setCellValueFactory(new PropertyValueFactory<>("alfa"));
        gammaTimeColumn.setCellValueFactory(new PropertyValueFactory<>("gamma"));

        nameMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        densityMaterialColumn.setCellValueFactory(new PropertyValueFactory<>("density"));

        calculationIdChColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cubesatColumn.setCellValueFactory(new PropertyValueFactory<>("cubesatName"));
        ntuColumn.setCellValueFactory(new PropertyValueFactory<>("ntuName"));
        orbitChColumn.setCellValueFactory(new PropertyValueFactory<>("height"));
        speedChColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        alfaChColumn.setCellValueFactory(new PropertyValueFactory<>("alfa"));
        forceXColumn.setCellValueFactory(new PropertyValueFactory<>("forceX"));
        coefficientXColumn.setCellValueFactory(new PropertyValueFactory<>("coefficientX"));
        momentXColumn.setCellValueFactory(new PropertyValueFactory<>("momentX"));
        forceYColumn.setCellValueFactory(new PropertyValueFactory<>("forceY"));
        coefficientYColumn.setCellValueFactory(new PropertyValueFactory<>("coefficientY"));
        momentYColumn.setCellValueFactory(new PropertyValueFactory<>("momentY"));
        velocityHeadColumn.setCellValueFactory(new PropertyValueFactory<>("velocityHead"));

        calculationIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        cubesatTimeColumn.setCellValueFactory(new PropertyValueFactory<>("cubesatName"));
        ntuTimeColumn.setCellValueFactory(new PropertyValueFactory<>("ntuName"));
        initOrbitColumn.setCellValueFactory(new PropertyValueFactory<>("initialOrbit"));
        finOrbitColumn.setCellValueFactory(new PropertyValueFactory<>("finalOrbit"));
        speedTimeChColumn.setCellValueFactory(new PropertyValueFactory<>("speed"));
        alfaTimeChColumn.setCellValueFactory(new PropertyValueFactory<>("alfa"));
        gammaTimeChColumn.setCellValueFactory(new PropertyValueFactory<>("gamma"));
        landTimeColumn.setCellValueFactory(new PropertyValueFactory<>("landTime"));
    }


    private List<NtuTableDto> getAllForNtuTable() {
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

    private List<AerodynamicCharacteristicsDto> getAllForAeroCharTable() {
        List<Object[]> rawData = aeroCharRepository.getAllForTable();
        List<AerodynamicCharacteristicsDto> dtos = new ArrayList<>();

        for (Object[] objects : rawData) {
            AerodynamicCharacteristicsDto dto = new AerodynamicCharacteristicsDto(
                    (Integer) objects[0],
                    (String) objects[1],
                    (String) objects[2],
                    (Double) objects[3],
                    (Double) objects[4],
                    (Double) objects[5],
                    (Double) objects[6],
                    (Double) objects[7],
                    (Double) objects[8],
                    (Double) objects[9],
                    (Double) objects[10],
                    (Double) objects[11],
                    (Double) objects[12]
            );
            dtos.add(dto);
        }
        return dtos;
    }

    private List<TimeCharacteristicsDto> getAllForTimeCharTable() {
        List<Object[]> rowData = timeCharacteristicsRepository.getAllForTable();
        List<TimeCharacteristicsDto> dtos = new ArrayList<>();

        for (Object[] objects : rowData) {
            TimeCharacteristicsDto dto = new TimeCharacteristicsDto(
                    (Integer) objects[0],
                    (String) objects[1],
                    (String) objects[2],
                    (Double) objects[3],
                    (Double) objects[4],
                    (Double) objects[5],
                    (Double) objects[6],
                    (Double) objects[7],
                    (Double) objects[8]
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
            Время спуска с орбиты %s км на орбиту %s км
            составило t = %s часов
            """;

    private final String cubesatChosen = "Длина - %s м, Ширина - %s м, Высота - %s м";
    private final String ntuChosen = "Длина - %s м, Радиус - %s м";
    private final String ntuChosenWithoutLength = "Радиус - %s м";
    private final String orbitChosen = "Высота - %s км, Скорость - %s м/с, Угол атаки - %s градусов";
    private final String orbitTimeChosen = """
            Нач. орбита - %s км, Фин. орбита - %s км, Скорость - %s м/с,
            Угол атаки - %s градусов, Угол наклона - %s градусов
            """;
}
