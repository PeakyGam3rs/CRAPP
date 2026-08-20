package com.svgs;

import com.svgs.framework.frontend.Preference;
import com.svgs.framework.frontend.PreferenceRegistry;

import eu.hansolo.medusa.GaugeBuilder;

public class GaugeCreator {
    public static void createGuage(String gaugeTitle) {
        String gaugeValue = PreferenceRegistry.getValueTitle(gaugeTitle);
        Preference p = PreferenceRegistry.getPreference(gaugeValue);
        
        GaugeBuilder b = GaugeBuilder.create();
        
        b.skinType(p.getSkinType());
        b.title(p.getTitle());
        b.unit(p.getUnit());
        b.angleRange(p.getAngleRange());
        b.minValue(p.getMinValue());
        b.maxValue(p.getMaxValue());
        b.threshold(p.getThreshold());
        b.thresholdVisible(p.getThresholdVisible());
        b.valueVisible(p.getValueVisible());
        b.barBackgroundColor(p.getColor());
        
        GaugeList.addGauge(b.build());
    }
}
