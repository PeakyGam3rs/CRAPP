package com.svgs;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.svgs.framework.app.DataProperty;
import com.svgs.framework.frontend.PreferenceRegistry;
import com.svgs.framework.reader.ReaderInterface;

import eu.hansolo.fx.charts.Axis;
import eu.hansolo.fx.charts.AxisType;
import eu.hansolo.fx.charts.ChartType;
import eu.hansolo.fx.charts.Grid;
import eu.hansolo.fx.charts.Position;
import eu.hansolo.fx.charts.XYChart;
import eu.hansolo.fx.charts.XYPane;
import eu.hansolo.fx.charts.data.XYChartItem;
import eu.hansolo.fx.charts.series.XYSeries;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

public class SaveViewer{
    private static final DateTimeFormatter timeStuff = DateTimeFormatter.ofPattern("HH:mm:ss");

    FileChooser fileChooser = new FileChooser();
    Path saveDir = Paths.get("src", "main", "java", "com", "svgs", "saves");
    private File selectedFile;
    private final ToggleGroup poop = new ToggleGroup();

    @FXML
    private Button saveSelector;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox space;

    @FXML
    private Button exitButton;

    @FXML
    private StackPane chartHolder;

    @FXML
    private VBox metricOptions;

    /**
     * Works out which CSV column holds a metric by asking DataRegistry where it sits.
     * getValueReadout writes the row in that same order, so the two can't drift apart
     * the way a hardcoded column list did.
     */
    private static int columnFor(String valueTitle) {
        List<DataProperty> bucket = ReaderInterface.dataBucket;
        for (int i = 0; i < bucket.size(); i++) {
            if (bucket.get(i).getTitleProperty().equals(valueTitle)) {
                return i + 1; // column 0 is the timestamp
            }
        }
        throw new IllegalArgumentException("'" + valueTitle + "' is not recorded to CSV");
    }

    @FXML
    void initialize() {
        for (String gaugeTitle : PreferenceRegistry.getPreferenceTitles()) {
            RadioButton button = new RadioButton(gaugeTitle);
            button.setToggleGroup(poop);
            // the value title, not a positional index, so the menu can be reordered freely
            button.setUserData(PreferenceRegistry.getValueTitle(gaugeTitle));
            metricOptions.getChildren().add(button);
        }

        poop.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> updateChart());
        if (!poop.getToggles().isEmpty()) {
            poop.selectToggle(poop.getToggles().get(0));
        }
    }

    @FXML
    void pickSave(ActionEvent event) {
        fileChooser.getExtensionFilters().setAll(new FileChooser.ExtensionFilter("CSV Files","*.csv"));
        fileChooser.setTitle("Select a Save.");
        File saveFolder = saveDir.toFile();
        if (saveFolder.exists()) {
            fileChooser.setInitialDirectory(saveFolder);
        }

        File selected = fileChooser.showOpenDialog(App.scene.getWindow());

        if(selected == null){return;}

        selectedFile = selected;
        saveSelector.setText(selectedFile.getName());
        updateChart();
    }

    private void updateChart() {
        Toggle selectedToggle = poop.getSelectedToggle();
        if (selectedFile == null || selectedToggle == null) {
            return;
        }

        String valueTitle = (String) selectedToggle.getUserData();
        try {
            chartHolder.getChildren().setAll(buildChart(selectedFile.toPath(), valueTitle));
        } catch (Exception e) {
            chartHolder.getChildren().clear();
            System.out.println("Could not load save graph");
            System.out.println(e);
        }
    }

    private XYChart<XYChartItem> buildChart(Path csvFile, String valueTitle) throws IOException {
        String label = PreferenceRegistry.getPreference(valueTitle).getTitle();
        List<XYChartItem> items = readChartItems(csvFile, columnFor(valueTitle));
        XYSeries<XYChartItem> series = new XYSeries<>(
            items,
            ChartType.LINE,
            label,
            Color.web("#31a6ff"),
            Color.web("#31a6ff"),
            false
        );

        double maxX = items.isEmpty() ? 1 : items.get(items.size() - 1).getX();
        double minY = items.stream().mapToDouble(XYChartItem::getY).min().orElse(0);
        double maxY = items.stream().mapToDouble(XYChartItem::getY).max().orElse(1);
        if (minY == maxY) {
            minY -= 1;
            maxY += 1;
        }

        Axis xAxis = new Axis(0, Math.max(maxX, 1), Orientation.HORIZONTAL, AxisType.LINEAR, Position.BOTTOM, "Time (seconds)");
        Axis yAxis = new Axis(minY, maxY, Orientation.VERTICAL, AxisType.LINEAR, Position.LEFT, label);
        xAxis.setAutoScale(true);
        yAxis.setAutoScale(true);

        XYPane<XYChartItem> pane = new XYPane<>(series);
        Grid grid = new Grid(xAxis, yAxis);
        XYChart<XYChartItem> chart = new XYChart<>(pane, grid, xAxis, yAxis);
        chart.setTitle(label);
        chart.setSubTitle(selectedFile.getName());
        chart.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return chart;
    }

    private List<XYChartItem> readChartItems(Path csvFile, int columnIndex) throws IOException {
        List<XYChartItem> items = new ArrayList<>();
        long firstTime = -1;

        try (BufferedReader reader = Files.newBufferedReader(csvFile)) {
            String line = reader.readLine(); // header row
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length <= columnIndex) {
                    continue;
                }

                long timeMillis = Long.parseLong(parts[0].trim());
                if (firstTime < 0) {
                    firstTime = timeMillis;
                }

                double elapsedSeconds = (timeMillis - firstTime) / 1000.0;
                double value = Double.parseDouble(parts[columnIndex].trim());
                LocalDateTime timestamp = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeMillis), ZoneId.systemDefault());
                items.add(new XYChartItem(elapsedSeconds, value, timestamp.format(timeStuff)));
            }
        }

        return items;
    }

    @FXML
    void leave(ActionEvent event) throws IOException{
        App.setRoot("mainScreen");
    }
}
