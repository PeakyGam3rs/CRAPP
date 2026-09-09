package com.svgs.framework.frontend;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.Gauge.SkinType;
import javafx.scene.paint.Color;

public class Preference {
    // all variables may not be initialized
    // therefore, follow AppPreferences and carefully null check.
    private Gauge.SkinType skinType;
    private String title;
    private String unit;
    private Integer angleRange;
    private Integer minValue;
    private Integer maxValue;
    private Integer threshold;
    private Boolean thresholdVisible;
    private Boolean valueVisible;
    private Color color;
    private Boolean isAnimated;

    public SkinType getSkinType() {
        if (this.skinType != null) {
            return this.skinType;
        } else {
            return AppPreferences.SKIN_TYPE;
        }
    }

    public String getTitle() {
        if (this.title != null) {
            return this.title;
        } else {
            return AppPreferences.TITLE;
        }
    }

    public String getUnit() {
        if (this.unit != null) {
            return this.unit;
        } else {
            return AppPreferences.UNIT;
        }
    }

    public int getAngleRange() {
        if (this.angleRange != null) {
            return this.angleRange;
        } else {
            return AppPreferences.ANGLE_RANGE;
        }
    }

    public int getMinValue() {
        if (this.minValue != null) {
            return this.minValue;
        } else {
            return AppPreferences.MIN_VALUE;
        }
    }

    public int getMaxValue() {
        if (this.maxValue != null) {
            return this.maxValue;
        } else {
            return AppPreferences.MAX_VALUE;
        }
    }

    public int getThreshold() {
        if (this.threshold != null) {
            return this.threshold;
        } else {
            return AppPreferences.THRESHOLD;
        }
    }

    public boolean getThresholdVisible() {
        if (this.thresholdVisible != null) {
            return this.thresholdVisible;
        } else {
            return AppPreferences.THRESHOLD_VISIBLE;
        }
    }

    public boolean getValueVisible() {
        if (this.valueVisible != null) {
            return this.valueVisible;
        } else {
            return AppPreferences.VALUE_VISIBLE;
        }
    }

    public Color getColor() {
        if (this.color != null) {
            return this.color;
        } else {
            return AppPreferences.COLOR;
        }
    }

    public boolean isAnimated() {
        if (this.isAnimated != null) {
            return this.isAnimated;
        } else {
            return AppPreferences.IS_ANIMATED;
        }
    }

    public void setSkinType(SkinType s) {
        this.skinType = s;
    }

    public void setTitle(String t) {
        this.title = t;
    }

    public void setUnit(String u) {
        this.unit = u;
    }

    public void setAngleRange(int a) {
        this.angleRange = a;
    }

    public void setMinValue(int v) {
        this.minValue = v;
    }

    public void setMaxValue(int v) {
        this.maxValue = v;
    }

    public void setThreshold(int t) {
        this.threshold = t;
    }

    public void setThresholdVisible(boolean t) {
        this.thresholdVisible = t;
    }

    public void setValueVisible(boolean v) {
        this.valueVisible = v;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setIsAnimated(boolean a) {
        this.isAnimated = a;
    }

}
