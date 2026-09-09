package com.svgs;

import com.svgs.framework.app.DataProperty;
import com.svgs.framework.frontend.Preference;
import com.svgs.framework.frontend.PreferenceRegistry;
import com.svgs.framework.reader.ReaderInterface;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;

/**
 * Turns a picked metric into a live gauge. This is the join between the three
 * halves: Preference says how it looks, DataProperty is where the number lands,
 * and registering the poll is what makes the number move.
 */
public class GaugeCreator {

    public static Gauge createGauge(String gaugeTitle) {
        String valueTitle = PreferenceRegistry.getValueTitle(gaugeTitle);
        Preference p = PreferenceRegistry.getPreference(valueTitle);
        DataProperty data = ReaderInterface.getProperty(valueTitle);

        GaugeBuilder<?> b = GaugeBuilder.create();

        b.skinType(p.getSkinType());
        b.title(p.getTitle());
        b.unit(p.getUnit());
        b.angleRange(p.getAngleRange());
        b.minValue(p.getMinValue());
        b.maxValue(p.getMaxValue());
        b.threshold(p.getThreshold());
        b.thresholdVisible(p.getThresholdVisible());
        b.valueVisible(p.getValueVisible());
        b.barColor(p.getColor());
        b.animated(p.isAnimated());

        Gauge gauge = b.build();

        // the gauge follows the property from here on. DataProperty.writeValue sets
        // this same object rather than replacing it, which is what keeps the bind
        // alive.
        gauge.valueProperty().bind(data.read());

        // nothing polls a metric until a gauge actually asks for it.
        ReaderInterface.registerPoll(data);

        GaugeList.addGauge(gauge);
        return gauge;
    }
}
