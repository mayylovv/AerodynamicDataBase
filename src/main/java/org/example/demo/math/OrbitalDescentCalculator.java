package org.example.demo.math;

import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.ode.FirstOrderIntegrator;
import org.apache.commons.math3.ode.nonstiff.ClassicalRungeKuttaIntegrator;
import org.example.demo.exception.IncorrectDataForMathException;

import static org.example.demo.util.Constant.EARTH_RADIUS;
import static org.example.demo.util.Constant.EXP_DECAY_RATE;
import static org.example.demo.util.Constant.G;
import static org.example.demo.util.Constant.LOG_DENSITY_AT_ZERO;
import static org.example.demo.util.Constant.M;

public class OrbitalDescentCalculator {

    // Параметры системы
    private final double R0; // Начальный радиус (м)
    private final double R_final; // Конечный радиус (м)
    private final double V0; // Начальная скорость (м/с)
    private final double theta0; // Начальный угол наклона (рад)
    private final double m; // Масса аппарата (кг, постоянная)
    private final double g_r0; // Гравитационное ускорение (м/с^2)
    private final double Omega; // Угловая скорость вращения Земли (рад/с)
    private final double sigma_x; // Коэффициент лобового сопротивления
    private final double sigma_y; // Коэффициент подъемной силы
    private final double area; // площадь

    // Конструктор для инициализации параметров
    public OrbitalDescentCalculator(double R0, double R_final, double V0, double theta0, double m, double g_r0, double Omega, double sigma_x, double sigma_y, double area) {
        this.R0 = R0;
        this.R_final = R_final;
        this.V0 = V0;
        this.theta0 = theta0;
        this.m = m;
        this.g_r0 = g_r0;
        this.Omega = Omega;
        this.sigma_x = sigma_x;
        this.sigma_y = sigma_y;
        this.area = area;
    }

    // Метод для расчета времени снижения
    public double calculateDescentTime() {
        System.out.println("R0: " + R0);
        System.out.println("R_final: " + R_final);
        System.out.println("V0: " + V0);
        System.out.println("theta0: " + theta0);
        System.out.println("m: " + m);
        System.out.println("g_r0: " + g_r0);
        System.out.println("Omega: " + Omega);
        System.out.println("sigma_x: " + sigma_x);
        System.out.println("sigma_y: " + sigma_y);

        // Начальные условия: [R, V, theta]
        double[] y0 = {R0, V0, theta0};

        // Создаем интегратор (метод Рунге-Кутта 4-го порядка)
        FirstOrderIntegrator integrator = new ClassicalRungeKuttaIntegrator(1.0); // Шаг 1 секунда

        // Создаем систему дифференциальных уравнений
        FirstOrderDifferentialEquations equations = new OrbitalEquations();

        // Интегрируем систему до достижения конечного радиуса
        StopEvent stopEvent = new StopEvent();
        integrator.addEventHandler(stopEvent, 1.0, 1.0e-6, 1000);

        double tEnd = 100000000L; // Увеличиваем конечное время интегрирования
        integrator.integrate(equations, 0, y0, tEnd, y0);

        double time = stopEvent.getStopTime();
        System.out.println("time = " + time);

        // Возвращаем время снижения
        return time;
    }

    // Система дифференциальных уравнений
    private class OrbitalEquations implements FirstOrderDifferentialEquations {
        @Override
        public int getDimension() {
            return 3; // 3 переменные: R, V, theta
        }

        /*@Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            double R = y[0];
            double V = y[1];
            double theta = y[2];

            if (Math.abs(V) < 1e-6 || Math.abs(m) < 1e-6) {
                throw new IncorrectDataForMathException("Velocity or mass is too close to zero: V = " + V + ", m = " + m);
            }

            // Плотность атмосферы
            double rho = Math.exp(EXP_DECAY_RATE * (R - EARTH_RADIUS) / 1000 + LOG_DENSITY_AT_ZERO);
            System.out.println("радиус" + (R - EARTH_RADIUS) / 1000 + "rho = "+ rho);

            // Уравнения для производных
            *//*yDot[0] = V * Math.sin(theta); // dR/dt
            yDot[1] = (-sigma_x * rho * V * V / m - g_r * Math.sin(theta)) + (R * Omega * Omega * Math.cos(theta)); // dV/dt
            yDot[2] = (sigma_y * rho * V / m - g_r * Math.cos(theta) / V); // dtheta/dt*//*


            double g_r = G * M / (R * R);

            *//*yDot[0] = V * Math.sin(theta);
            yDot[1] = (-sigma_x * rho * V * V / m - g_r * Math.sin(theta) + (R * Omega * Omega * Math.cos(theta)));
            yDot[2] = (sigma_y * rho * V / m - g_r * Math.cos(theta) / V);*//*

            yDot[0] = V * Math.sin(theta);
            yDot[1] = (-sigma_x * rho * V * V / m - g_r * Math.sin(theta)) + (R * Omega * Omega * Math.cos(theta));
            yDot[2] = (sigma_y * rho * V / m - g_r * Math.cos(theta) / V);

        }*/

        @Override
        public void computeDerivatives(double t, double[] y, double[] yDot) {
            double R = y[0];
            double V = y[1];
            double theta = y[2];

            if (Math.abs(V) < 1e-6 || Math.abs(m) < 1e-6) {
                throw new IncorrectDataForMathException("Velocity or mass is too close to zero: V = " + V + ", m = " + m);
            }

            // Плотность атмосферы (экспоненциальная модель)
            double rho = Math.exp(EXP_DECAY_RATE * (R - EARTH_RADIUS) / 1000 + LOG_DENSITY_AT_ZERO);

            // Гравитационное ускорение
            double g_r = G * M / (R * R);

            // Сила аэродинамического сопротивления
            double F_drag = 0.5 * rho * V * V * sigma_x * area;

            // Уравнения для производных
            yDot[0] = V * Math.sin(theta); // dR/dt
            yDot[1] = -F_drag / m - g_r * Math.sin(theta); // dV/dt
            yDot[2] = -g_r * Math.cos(theta) / V; // dtheta/dt
        }
    }

    // Событие для остановки интегрирования при достижении конечного радиуса
    private class StopEvent implements org.apache.commons.math3.ode.events.EventHandler {
        private double stopTime = Double.NaN;

        @Override
        public void init(double t0, double[] y0, double t) {}

        @Override
        public double g(double t, double[] y) {
            return y[0] - R_final; // Остановка при R <= R_final
        }

        @Override
        public Action eventOccurred(double t, double[] y, boolean increasing) {
            stopTime = t; // Сохраняем время остановки
            return Action.STOP; // Остановить интегрирование
        }

        @Override
        public void resetState(double t, double[] y) {}

        public double getStopTime() {
            return stopTime;
        }
    }
}