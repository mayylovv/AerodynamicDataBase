package org.example.demo.service;

import org.example.demo.util.Constant;
import org.springframework.stereotype.Component;

import static java.lang.Math.sqrt;

@Component
public class SpeedCalculator {

    public double calculateSpeed(double heightKm, double overSpeed) {
        return overSpeed + sqrt(Constant.GRAVITY_CONSTANT * Constant.EARTH_MASS / (heightKm + Constant.EARTH_RADIUS));
    }
}
