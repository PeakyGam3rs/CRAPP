package com.svgs.framework.app;

import java.util.ArrayList;

import javafx.beans.property.SimpleDoubleProperty;

public class DataRegistry {
    public static ArrayList<DataProperty> createData() {
        ArrayList<DataProperty> result = new ArrayList<>();
        result.add(new DataProperty("boostValue", new SimpleDoubleProperty(), "010B", 1));
        result.add(new DataProperty("revValue", new SimpleDoubleProperty(), "010C", 2));
        result.add(new DataProperty("trimValue", new SimpleDoubleProperty(), "0106", 1));
        result.add(new DataProperty("fuelPressureValue", new SimpleDoubleProperty(), "010A", 1));
        result.add(new DataProperty("coolantValue", new SimpleDoubleProperty(), "0105", 1));
        result.add(new DataProperty("loadValue", new SimpleDoubleProperty(), "0104", 1));
        result.add(new DataProperty("speedValue", new SimpleDoubleProperty(), "010D", 1));
        result.add(new DataProperty("throttleValue", new SimpleDoubleProperty(), "0111", 1));
        result.add(new DataProperty("timingValue", new SimpleDoubleProperty(), "010E", 1));

        return result;
    }
}
