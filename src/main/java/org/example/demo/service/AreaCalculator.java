package org.example.demo.service;

import lombok.RequiredArgsConstructor;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.example.demo.config.FormulaLoader;
import org.example.demo.entity.CharacteristicsNtu;
import org.example.demo.entity.CubesatSize;
import org.example.demo.entity.MaterialInfoEntity;
import org.example.demo.exception.NotFoundException;
import org.example.demo.repository.MaterialInfoRepository;
import org.springframework.stereotype.Component;

import static java.lang.Math.pow;

@Component
@RequiredArgsConstructor
public class AreaCalculator {

    private final MaterialInfoRepository materialInfoRepository;

    public double calculateArea(String name, double radius, double length, double alfa) {
        String formula = FormulaLoader.loadFormula(name, "area");
        if (formula == null) {
            throw new NotFoundException("Формула для формы: " + name + " не найдена");
        }
        return evaluateFormula(formula, radius, length, 0, 0, 0, alfa);
    }

    public double calculateMidsectionArea(double radius, String formName, double length, double alfa) {
        String formula = FormulaLoader.loadFormula(formName, "midsectionArea");
        if (formula == null) {
            throw new NotFoundException("Формула для формы: " + formName + " не найдена");
        }
        return evaluateFormula(formula, radius, length, 0, 0, 0, alfa);
    }

    public double calculateLevelArm(String formName, double radius, double length, CubesatSize cubesatSize, CharacteristicsNtu charNtu, double alfa) {
        double[] centrePressure = calculateCentrePressure(formName, radius, length, cubesatSize, alfa);
        double[] centreMass = calculateCentreMass(formName, radius, length, cubesatSize, charNtu, alfa);
        return centrePressure[0] - centreMass[0];
    }

    private double[] calculateCentrePressure(String formName, double radius, double length, CubesatSize cubesatSize, double alfa) {
        String formulaX = FormulaLoader.loadFormula(formName, "centrePressureX");
        String formulaY = FormulaLoader.loadFormula(formName, "centrePressureY");
        String formulaZ = FormulaLoader.loadFormula(formName, "centrePressureZ");

        double x = evaluateFormula(formulaX, radius, length, cubesatSize.getLength(), 0, 0, alfa);
        double y = evaluateFormula(formulaY, radius, length, 0, cubesatSize.getWidth(), 0, alfa);
        double z = evaluateFormula(formulaZ, radius, length, 0, 0, cubesatSize.getHeight(), alfa);

        return new double[]{x, y, z};
    }

    private double[] calculateCentreMass(String formName, double radius, double length, CubesatSize cubesatSize, CharacteristicsNtu characteristicsNtu, double alfa) {
        MaterialInfoEntity materialOfNtu = materialInfoRepository.findById(characteristicsNtu.getMaterial().getId()).get();

        String formulaX = FormulaLoader.loadFormula(formName, "centreMassX");
        String formulaY = FormulaLoader.loadFormula(formName, "centreMassY");
        String formulaZ = FormulaLoader.loadFormula(formName, "centreMassZ");

        double x = evaluateFormula(formulaX, radius, length, cubesatSize.getLength(), 0, 0, alfa);
        double y = evaluateFormula(formulaY, radius, length, 0, cubesatSize.getWidth(), 0, alfa);
        double z = evaluateFormula(formulaZ, radius, length, 0, 0, cubesatSize.getHeight(), alfa);

        double shellNtu = calculateShellNtu(formName, radius, length, alfa);
        double massCs = cubesatSize.getMass();
        double massArea = materialOfNtu.getDensity() * characteristicsNtu.getThickness() * pow(10, -6) * shellNtu;

        return new double[]{
                (massCs * x + massArea * x) / (massCs + massArea),
                (massCs * y + massArea * y) / (massCs + massArea),
                (massCs * z + massArea * z) / (massCs + massArea)
        };
    }

    public double calculateShellNtu(String formName, double radius, double length, double alfa) {
        return evaluateFormula(FormulaLoader.loadFormula(formName, "shellNtu"), radius, length, 0, 0, 0, alfa);
    }


    private double evaluateFormula(String formula, double radius, double length, double cubesatSizeLength, double cubesatSizeWidth, double cubesatSizeHeight, double alfa) {
        Expression expression = new ExpressionBuilder(formula)
                .variables("radius", "length", "PI", "cubesatSize.length", "cubesatSize.width", "cubesatSize.height", "alfa")
                .build()
                .setVariable("radius", radius)
                .setVariable("length", length)
                .setVariable("PI", Math.PI)
                .setVariable("cubesatSize.length", cubesatSizeLength)
                .setVariable("cubesatSize.width", cubesatSizeWidth)
                .setVariable("cubesatSize.height", cubesatSizeHeight)
                .setVariable("alfa", alfa);
        return expression.evaluate();
    }
}
