package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.util.Constant;
import org.springframework.stereotype.Service;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static org.example.demo.util.Constant.EXP_DECAY_RATE;
import static org.example.demo.util.Constant.LOG_DENSITY_AT_ZERO;

@Service
@RequiredArgsConstructor
public class MainCalculationService {

    private final SpeedCalculator speedCalculator;
    private final AreaCalculator areaCalculator;

    public double calculateForceX(double heightKm, double radius, double length, double alfa, String formName, double speed) {
        double density = getDensity(heightKm);
        double coefficientX = calculateCoefficientX(radius, length, formName, alfa);
        double area = calculateArea(formName, radius, length, alfa);

        return 0.5 * density * pow(speed, 2) * coefficientX * area;
    }

    public double calculateMomentX(double heightKm, double radius, double length, double alfa, double speed, String formName, CubesatSize cubesatSize, CharacteristicsNtu charNtu) {
        double coefficientX = calculateCoefficientX(radius, length, formName, alfa);
        double density = getDensity(heightKm);
        double midsectionArea = calculateMidsectionArea(radius, formName, length, alfa);
        double levelArm = calculateLevelArm(formName, radius, length, cubesatSize, charNtu, alfa);

        return -0.5 * coefficientX * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateForceY(double heightKm, double radius, double length, double alfa, String formName, double speed) {
        double density = getDensity(heightKm);
        double coefficientY = calculateCoefficientY(alfa);
        double area = calculateArea(formName, radius, length, alfa);

        return 0.5 * density * pow(speed, 2) * coefficientY * area;
    }

    public double calculateMomentY(double heightKm, double radius, double alfa, double length, double speed, String formName, CubesatSize cubesatSize, CharacteristicsNtu charNtu) {
        double coefficientY = calculateCoefficientY(alfa);
        double density = getDensity(heightKm);
        double midsectionArea = calculateMidsectionArea(radius, formName, length, alfa);
        double levelArm = calculateLevelArm(formName, radius, length, cubesatSize, charNtu, alfa);

        return -0.5 * coefficientY * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateVelocityHead(double heightKm, double speed) {
        // скоростной напор
        double density = getDensity(heightKm);
        return 0.5 * density * pow(speed, 2);
    }

    /////////////////////////////////////////////////////////

    public double calculateCoefficientX(double radius, double length, String formName, double alfa) {
        return Constant.FREE_MOLECULAR_DRAG_COEFFICIENT * calculateArea(formName, radius, length, alfa) / calculateMidsectionArea(radius, formName, length, alfa);
    }

    public double calculateCoefficientY(double alfa) {
        return 2 * sin(alfa) * cos(alfa);
    }

    /////////////////////////////////////////////////////////

    // тут в кг/м3
    public double getDensity(double heightKm) {
        return Math.exp(EXP_DECAY_RATE * heightKm + LOG_DENSITY_AT_ZERO);
    }

    public double calculateMinSpeed(double heightKm) {
        return speedCalculator.calculateMinSpeed(heightKm);
    }

    public double calculateLevelArm(String formName, double radius, double length, CubesatSize cubesatSize, CharacteristicsNtu charNtu, double alfa) {
        return areaCalculator.calculateLevelArm(formName, radius, length, cubesatSize, charNtu, alfa);
    }

    public double calculateArea(String name, double radius, double length, double alfa) {
        return areaCalculator.calculateArea(name, radius, length, alfa);
    }

    public double calculateMidsectionArea(double radius, String formName, double length, double alfa) {
        return areaCalculator.calculateMidsectionArea(radius, formName, length, alfa);
    }

    public double calculateFullMass(CubesatSize cubesatSize, CharacteristicsNtu charNtu, double alfa) {
        String formName = charNtu.getForm().getFileName();
        double radius = charNtu.getRadius();
        double length = charNtu.getLength();
        double materialDensity = charNtu.getMaterial().getDensity();
        double shellNtu = areaCalculator.calculateShellNtu(formName, radius, length, alfa);

        double massCubesat = cubesatSize.getMass();
        double massNtu = materialDensity * charNtu.getThickness() * pow(10, -6) * shellNtu;

        return massNtu + massCubesat;
    }
}
