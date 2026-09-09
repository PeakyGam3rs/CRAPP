package com.svgs;

import java.io.IOException;

import eu.hansolo.medusa.Gauge;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class PrimaryController {

    @FXML
    private Button addGaugeButton;

    @FXML
    private HBox vbux;

    @FXML
    private Button saveButton;

    /**
     * Rebuilds the dashboard from GaugeList. Coming back from the picker throws
     * this
     * controller away and makes a new one, so the gauges get re-hung every time
     * rather than being created here.
     */
    @FXML
    void initialize() {
        vbux.getChildren().clear();

        for (Gauge gauge : GaugeList.getGauges()) {
            HBox.setHgrow(gauge, Priority.ALWAYS);
            vbux.getChildren().add(gauge);
        }
    }

    @FXML
    void addGaugeScreen(ActionEvent event) throws IOException {
        App.setRoot("gaugeList");
    }

    @FXML
    void goToSaves(ActionEvent event) throws IOException {
        App.setRoot("saveView");
    }
}
