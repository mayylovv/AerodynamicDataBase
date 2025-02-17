package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.exception.NotFoundException;
import org.example.demo.repository.CharacteristicsNtuRepository;
import org.example.demo.repository.CubesatSizeRepository;
import org.example.demo.repository.MaterialInfoRepository;
import org.springframework.stereotype.Component;

import static java.lang.Math.PI;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

@Component
@RequiredArgsConstructor
public class AreaCalculator {

    private final CubesatSizeRepository cubesatSizeRepository;
    private final MaterialInfoRepository materialInfoRepository;
    private final CharacteristicsNtuRepository characteristicsNtuRepository;

    public double calculateArea(String name, double radius, double length, double alfa) {
        double smallRadius = sqrt(pow(radius, 2) + pow(length, 2) / 4);
        double tinyRadius = sqrt(pow(smallRadius, 2) - pow(length, 2) / 4) - length / 2;
        double areaEllipse = Math.PI * smallRadius * (smallRadius * Math.cos(alfa) + tinyRadius * (1 - Math.cos(alfa)));

        return switch (name) {
            case "ШАР" -> pow(radius, 2) * Math.PI;
            case "КОНУС" -> cos(alfa) * (Math.PI * radius * radius) + sin(alfa) * (radius * length * 0.5);
            case "ЧЕЧЕВИЦА" -> areaEllipse;
            default -> throw new NotFoundException("НТУ с названием: " + name + " нет");
        };
    }

    public double calculateMidsectionArea(double radius, String formName, double length) {
        double smallRadius = sqrt(pow(radius, 2) + pow(length, 2) / 4);
        return switch (formName) {
            case "ШАР" -> pow(radius, 2) * Math.PI;
            case "КОНУС" -> pow(radius, 2) * Math.PI;
            case "ЧЕЧЕВИЦА" -> pow(smallRadius, 2) * Math.PI;
            default -> throw new NotFoundException("НТУ с названием: " + formName + " нет");
        };
    }

    public double calculateLevelArm(String formName, double radius, double length, int cubesatId, int charNtuId) {
        double[] centrePressure = calculateCentrePressure(formName, radius, length, cubesatId);
        double[] centreMass = calculateCentreMass(formName, radius, length, cubesatId, charNtuId);
        return centrePressure[0] - centreMass[0];
    }

    private double[] calculateCentrePressure(String formName, double radius, double length, int cubesatId) {
        CubesatSize cubesatSize = cubesatSizeRepository.findById(cubesatId).orElseThrow(RuntimeException::new);
        double[] centreCS = {cubesatSize.getLength() / 2, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
        double[] centreArea = switch (formName) {
            case "ШАР" ->
                    new double[]{cubesatSize.getLength() + radius, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            case "КОНУС" ->
                    new double[]{cubesatSize.getLength() + length / 3, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            case "ЧЕЧЕВИЦА" ->
                    new double[]{cubesatSize.getLength() + radius - length / 2, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            default -> throw new NotFoundException("Неизвестная форма: " + formName);
        };
        return new double[]{(centreArea[0] - centreCS[0]) / 2, (centreArea[1] - centreCS[1]) / 2, (centreArea[1] - centreCS[1]) / 2};
    }

    private double[] calculateCentreMass(String formName, double radius, double length, int cubesatId, int charNtuId) {
        CubesatSize cubesatSize = cubesatSizeRepository.findById(cubesatId).orElseThrow(RuntimeException::new);
        CharacteristicsNtu characteristicsNtu = characteristicsNtuRepository.findById(charNtuId).get();
        MaterialInfoEntity materilaOfNtu = materialInfoRepository.findById(characteristicsNtu.getMaterialId()).get();

        double[] centreCs = {cubesatSize.getXMass(), cubesatSize.getYMass(), cubesatSize.getZMass()};
        double[] centreArea = switch (formName) {
            case "ШАР" ->
                    new double[]{cubesatSize.getLength() + radius, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            case "КОНУС" ->
                    new double[]{cubesatSize.getLength() + length / 4, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            case "ЧЕЧЕВИЦА" ->
                    new double[]{cubesatSize.getLength() + radius - length / 2, cubesatSize.getWidth() / 2, cubesatSize.getHeight() / 2};
            default -> throw new NotFoundException("Неизвестная форма: " + formName);
        };
        double shellNtu = switch (formName) {
            case "ШАР" -> characteristicsNtu.getRadius() * characteristicsNtu.getRadius() * 4 * PI;
            case "КОНУС" -> PI * characteristicsNtu.getRadius() * sqrt(Math.pow(characteristicsNtu.getRadius(), 2) +
                    Math.pow(characteristicsNtu.getLength(), 2)) + PI + Math.pow(characteristicsNtu.getRadius(), 2);
            case "ЧЕЧЕВИЦА" ->
                    4 * PI * characteristicsNtu.getRadius() * characteristicsNtu.getRadius() * (characteristicsNtu.getRadius()
                            - sqrt(pow(characteristicsNtu.getRadius(), 2) - pow(characteristicsNtu.getLength(), 2) / 4));
            default -> throw new NotFoundException("Неизвестная форма: " + formName);
        };

        double massCs = cubesatSize.getMass();
        double massArea = materilaOfNtu.getDensity() * characteristicsNtu.getThickness() * shellNtu;

        return new double[]{
                (massCs * centreCs[0] + massArea * centreArea[0]) / (massCs + massArea),
                (massCs * centreCs[1] + massArea * centreArea[1]) / (massCs + massArea),
                (massCs * centreCs[2] + massArea * centreArea[2]) / (massCs + massArea)
        };
    }
}
