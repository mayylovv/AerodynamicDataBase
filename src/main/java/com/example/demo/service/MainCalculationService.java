package com.example.demo.service;

import com.example.demo.entity.Atmosphere;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.AtmosphereRepository;
import com.example.demo.util.Constant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static java.lang.Math.*;

@Service
@RequiredArgsConstructor
public class MainCalculationService {

    private final AtmosphereRepository atmosphereRepository;

    public double calculateForceX(int heightKm, double radius) {
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm);
        double coefficientX = calculateCoefficientX(radius);
        double area = calculateArea(radius);

        return 0.5 * density * pow(speed,2) * coefficientX * area;
    }

    public double calculateMomentX(int heightKm, double radius, double alfa, double length) {

        double coefficientX = calculateCoefficientX(radius);
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm);
        double midsectionArea = calculateMidsectionArea(radius);
        double levelArm = calculateLevelArm(alfa, radius, length);

        return - 0.5 * coefficientX * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateForceY(int heightKm, double radius, double alfa) {
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm);
        double coefficientY = calculateCoefficientY(alfa);
        double area = calculateArea(radius);

        return 0.5 * density * pow(speed, 2) * coefficientY * area;
    }

    public double calculateMomentY(int heightKm, double radius, double alfa, double length) {
        double coefficientY = calculateCoefficientY(alfa);
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm);
        double midsectionArea = calculateMidsectionArea(radius);
        double levelArm = calculateLevelArm(alfa, radius, length);

        return - 0.5 * coefficientY * density * pow(speed, 2) * midsectionArea * levelArm;
    }

    public double calculateVelocityHead(int heightKm) {
        // скоростной напор
        double density = getDensity(heightKm);
        double speed = calculateSpeed(heightKm);
        return 0.5 * density * pow(speed, 2);
    }

    /////////////////////////////////////////////////////////

    public double calculateCoefficientX(double radius) {
        // пока только шар
        double coefficient = Constant.FREE_MOLECULAR_DRAG_COEFFICIENT * pow(radius, 2) * Math.PI;;
        return coefficient;
    }

    public double calculateCoefficientY(double alfa) {
        double radians = Math.toRadians(alfa);
        return 2 * sin(radians) * cos(radians);
    }

    /////////////////////////////////////////////////////////

    public double getDensity(int heightKm) {
        Atmosphere atmosphere = atmosphereRepository.findByHeightKm(heightKm)
                .orElseThrow(() -> new NotFoundException("В базе данных нет характеристик для высоты: " + heightKm + "км"));
        return atmosphere.getDensity();
    }

    public double calculateSpeed(double heightKm) {
        return sqrt(Constant.GRAVITY_CONSTANT * Constant.EARTH_MASS / (heightKm * 1000 + Constant.EARTH_RADIUS));
    }

    public double calculateLevelArm(double alfa, double radius, double length) {
        // только для шара
        double radians = Math.toRadians(alfa);
        return (radius + length) * sin(radians);
    }

    public double calculateArea(double radius) {
        // только шар
        return pow(radius, 2) * Math.PI;
    }

    public double calculateMidsectionArea(double radius) {
        return pow(radius, 2) * Math.PI;
    }

}
