package org.example.demo.archive;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.text.Font;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

public class ChartController {
    @FXML
    private LineChart<Number, Number> lineChart;

    @FXML
    private NumberAxis xAxis, yAxis;


    @FXML
    private void initialize() {
        xAxis.setLowerBound(0);
        xAxis.setUpperBound(400);
        xAxis.setTickLabelFont(new Font(22));
        xAxis.setAutoRanging(false);

        yAxis.setLowerBound(290);
        yAxis.setUpperBound(410);
        yAxis.setTickLabelFont(new Font(22));
        yAxis.setAutoRanging(false);
        populateChart();
    }

    private void populateChart() {
        double[] descentTimes = {383.44, 373.45, 380.45};

        XYChart.Series<Number, Number> coneSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> sphereSeries = new XYChart.Series<>();
        XYChart.Series<Number, Number> lentilSeries = new XYChart.Series<>();

        coneSeries.setName("Конус1");
        sphereSeries.setName("Сфера1");
        lentilSeries.setName("Чечевица1");

        int steps = 5;
        double startHeight = 400.0;
        double endHeight = 300.0;

        for (int i = 0; i <= steps; i++) {
            double time = (i * (descentTimes[0] / steps));
            double heightCone = startHeight - (startHeight - endHeight) * (i / (double) steps);
            coneSeries.getData().add(new XYChart.Data<>(time, heightCone));

            time = (i * (descentTimes[1] / steps));
            double heightSphere = startHeight - (startHeight - endHeight) * (i / (double) steps);
            sphereSeries.getData().add(new XYChart.Data<>(time, heightSphere));

            time = (i * (descentTimes[2] / steps));
            double heightLentil = startHeight - (startHeight - endHeight) * (i / (double) steps);
            lentilSeries.getData().add(new XYChart.Data<>(time, heightLentil));
        }

        lineChart.getData().addAll(coneSeries, sphereSeries, lentilSeries);
    }
}
