package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.entity.Atmosphere;
import org.example.demo.exception.NotFoundException;
import org.example.demo.repository.AtmosphereRepository;
import org.example.demo.util.Constant;
import org.springframework.stereotype.Service;

import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;

@Service
@RequiredArgsConstructor
public class MainCalculationService {

    private final AtmosphereRepository atmosphereRepository;
    private final SpeedCalculator speedCalculator;
    private final AreaCalculator areaCalculator;

    public double calculateForceX(int heightKm, double radius, double length, double alfa, String formName, double overSpeed) {
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm, overSpeed);
        double coefficientX = calculateCoefficientX(radius, length, alfa, formName);
        double area = calculateArea(formName, radius, length, alfa);

        return 0.5 * density * pow(speed,2) * coefficientX * area;
    }

    public double calculateMomentX(int heightKm, double radius, double alfa, double length, double overSpeed, String formName, int cubesatId, int charNtuId) {
        double coefficientX = calculateCoefficientX(radius, length, alfa, formName);
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm, overSpeed);
        double midsectionArea = calculateMidsectionArea(radius, formName, length);
        double levelArm = calculateLevelArm(formName, radius, length, cubesatId, charNtuId);

        return - 0.5 * coefficientX * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateForceY(int heightKm, double radius, double length, double alfa, String formName, double overSpeed) {
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm, overSpeed);
        double coefficientY = calculateCoefficientY(alfa);
        double area = calculateArea(formName, radius, length, alfa);

        return 0.5 * density * pow(speed, 2) * coefficientY * area;
    }

    public double calculateMomentY(int heightKm, double radius, double alfa, double length, double overSpeed, String formName, int cubesatId, int charNtuId) {
        double coefficientY = calculateCoefficientY(alfa);
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm, overSpeed);
        double midsectionArea = calculateMidsectionArea(radius, formName, length);
        double levelArm = calculateLevelArm(formName, radius, length, cubesatId, charNtuId);

        return - 0.5 * coefficientY * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateVelocityHead(int heightKm, double overSpeed) {
        // скоростной напор
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm, overSpeed);
        return 0.5 * density * pow(speed, 2);
    }

    /////////////////////////////////////////////////////////

    public double calculateCoefficientX(double radius, double length, double alfa, String formName) {
        return Constant.FREE_MOLECULAR_DRAG_COEFFICIENT * calculateArea(formName, radius, length, alfa);
    }

    public double calculateCoefficientY(double alfa) {
        return 2 * sin(alfa) * cos(alfa);
    }

    /////////////////////////////////////////////////////////

    public double getDensity(int heightKm) {
        Atmosphere atmosphere = atmosphereRepository.findByHeightKm(heightKm)
                .orElseThrow(() -> new NotFoundException("В базе данных нет характеристик для высоты: " + heightKm + "км"));
        return atmosphere.getDensity();
    }

    public double calculateSpeed(double heightKm, double overSpeed) {
        return speedCalculator.calculateSpeed(heightKm, overSpeed);
    }

    public double calculateLevelArm(String formName, double radius, double length, int cubesatId, int charNtuId) {
        return areaCalculator.calculateLevelArm(formName, radius, length, cubesatId, charNtuId);
    }

    public double calculateArea(String name, double radius, double length, double alfa) {
        return areaCalculator.calculateArea(name, radius, length, alfa);
    }

    public double calculateMidsectionArea(double radius, String formName, double length) {
        return areaCalculator.calculateMidsectionArea(radius, formName, length);
    }

}
