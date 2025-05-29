package org.example.demo.controller;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChartController {
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis, yAxis;


    @FXML
    private void initialize() {
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(500);
        xAxis.setTickLabelFont(new Font(22));
        xAxis.setAutoRanging(false);

        yAxis.setLowerBound(150);
        yAxis.setUpperBound(550);
        yAxis.setTickLabelFont(new Font(22));
        yAxis.setAutoRanging(false);
        populateChart();
    }

    private void populateChart() {
        double[] descentTimes = {37, 202, 472};

        XYChart.Series<Number, Number> coneSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> sphereSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> lentilSeries = new XYChart.Series<>();

        coneSeries.setName("Конус1");
        sphereSeries.setName("Конус2");
        lentilSeries.setName("Конус3");

        int steps = 5;
        double startHeight = 300.0;
        double endHeight = 200.0;
        double startHeight1 = 400.0;
        double endHeight1 = 300.0;
        double startHeight2 = 500.0;
        double endHeight2 = 400.0;

        for (int i = 0; i <= steps; i++) {
            double time = (i * (descentTimes[0] / steps));
            double heightCone = startHeight - (startHeight - endHeight) * (i / (double) steps);
            coneSeries.getData().add(new XYChart.Data<>(time, heightCone));

            time = (i * (descentTimes[1] / steps));
            double heightSphere = startHeight1 - (startHeight1 - endHeight1) * (i / (double) steps);
            sphereSeries.getData().add(new XYChart.Data<>(time, heightSphere));

            time = (i * (descentTimes[2] / steps));
            double heightLentil = startHeight2 - (startHeight2 - endHeight2) * (i / (double) steps);
            lentilSeries.getData().add(new XYChart.Data<>(time, heightLentil));
        }

        lineChart.getData().addAll(coneSeries, sphereSeries, lentilSeries);
    }
}
