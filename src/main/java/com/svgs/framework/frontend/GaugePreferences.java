package com.svgs.framework.frontend;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import eu.hansolo.medusa.GaugeBuilder;
import javafx.event.ActionEvent;
import javafx.scene.paint.Color;

public class GaugePreferences {
    public Guage createGuage(ActionEvent event) {
        Gauge gauge = GaugeBuilder.create()
            .skinType(SkinType.BULLET_CHART) // Choose a skin type (e.g., LEVEL, MODERN, AMP, etc.)
            .prefSize(400, 400)
            .title("Temperature")
            .unit("°C")
            .minValue(0)
            .maxValue(100)
            .barColor(Color.RED)
            .animated(true)
            .build();
        return gauge;
    }

    public Preference selectGauge(String valueProperty) {
        try {
            Gauge gauge = GaugeBuilder.create();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
