package org.example.demo.math;

import lombok.Setter;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.springframework.stereotype.Component;

import static org.example.demo.util.Constant.ACCELERATION_OF_GRAVITY;
import static org.example.demo.util.Constant.RADIUS_EARTH;


@Component
@Setter
public class ReentryEquations implements FirstOrderDifferentialEquations {

    @Setter
    private double forceX;
    @Setter
    private double forceY;
    @Setter
    private double mass;

    @Override
    public int getDimension() {
        return 3; // Три переменные: R, V, θ
    }

    @Override
    public void computeDerivatives(double t, double[] y, double[] yDot) {
        double R = y[0];  // Радиус
        double V = y[1];  // Скорость
        double theta = y[2]; // Угол наклона траектории

        double actualAccelGravity = ACCELERATION_OF_GRAVITY * Math.pow(RADIUS_EARTH, 2) / Math.pow(R, 2);

        yDot[0] = V * Math.sin(theta); // dR/dt = V * sin(θ)
        yDot[1] = -actualAccelGravity * Math.sin(theta) - (forceX / mass) + (forceY / mass) * Math.cos(theta); // dV/dt
        yDot[2] = -(actualAccelGravity / V) * Math.cos(theta) + (forceX / (mass * V)) * Math.sin(theta) + (forceY / (mass * V)); // dθ/dt
    }

}
