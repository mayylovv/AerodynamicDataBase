package com.example.demo.service;

import com.example.demo.entity.CubesatSize;
import com.example.demo.exception.NotFoundException;
import com.example.demo.repository.CubesatSizeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static java.lang.Math.pow;
import static java.lang.Math.sin;

@Component
@RequiredArgsConstructor
public class AreaCalculator {

    private final CubesatSizeRepository cubesatSizeRepository;

    public double calculateArea(String name, double radius, double alfa) {
        return switch (name) {
            case "ШАР" ->
                // при любом угле аттаки и скольжения проекция шара будет одинаковой
                    pow(radius, 2) * Math.PI;
            case "КОНУС" -> 0;
            case "ЧЕЧЕВИЦА" -> 1;
            default -> throw new RuntimeException("НТУ с названием: " + name + " нет");
        };
    }

    public double calculateMidsectionArea(double radius) {
        return pow(radius, 2) * Math.PI;
    }

    public double calculateLevelArm(int height, String name, double radius, double alfa) {
        return switch (name) {
            case "ШАР" -> radius * sin(alfa);

            case "КОНУС" -> 0;
            case "ЧЕЧЕВИЦА" -> 1;
            default -> throw new RuntimeException("НТУ с названием: " + name + " нет");
        };
    }


    // площадь наибольшей проекции кубсата 3у равна 0.03м3, площадь проекции шара радиусом 3м равно 27.85м3
    // перенебрегаем площадью кубсата
}
