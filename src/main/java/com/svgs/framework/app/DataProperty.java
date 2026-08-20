package com.svgs.framework.app;

import javafx.beans.property.SimpleDoubleProperty;

public class DataProperty {
    private SimpleDoubleProperty data;
    private final String title;
    private final String readProperty;
    private final int dataOffset;
    public DataProperty(String title, SimpleDoubleProperty data, String readProperty, int dataOffset) {
        this.data = data;
        this.title = title;
        this.readProperty = readProperty;
        this.dataOffset = dataOffset;
    }

    public SimpleDoubleProperty read() {
        return data;
    }

    public void writeValue(double value) {
        data = new SimpleDoubleProperty(value);
    }

    public String getTitleProperty() {
        return title;
    }

    public String getReadProperty() {
        return readProperty;
    }
    public int getDataOffset() {
        return dataOffset;
    }
}
