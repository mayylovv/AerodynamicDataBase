package org.example.demo.math;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReentrySimulation {

    private final ReentryEquations reentryEquations;

    @Autowired
    public ReentrySimulation(ReentryEquations reentryEquations) {
        this.reentryEquations = reentryEquations;
    }

    public double calculateTime(double startHeight, double endHeight, double speed, double theta0, double forceX, double forceY, double mass) {

        reentryEquations.setForceX(forceX);
        reentryEquations.setForceY(forceY);
        reentryEquations.setMass(mass);

        // Определение системы уравнений
        FirstOrderDifferentialEquations equations = new ReentryEquations();

        // Интегратор (Рунге-Кутта 8-го порядка)
        FirstOrderIntegrator integrator = new DormandPrince853Integrator(1, 60, 1e-8, 1e-8);

        // Массивы для хранения значений (y[0] = R, y[1] = V, y[2] = theta)
        double[] y = {startHeight, speed, theta0};
        double[] yFinal = new double[3];

        // Интеграция по времени, пока R > R_end
        double time = 0;
        double dt = 1; // Шаг по времени (сек)
        while (y[0] > endHeight) {
            integrator.integrate(equations, time, y, time + dt, yFinal);
            time += dt;
            System.arraycopy(yFinal, 0, y, 0, y.length);
        }

        return time;
    }
}
