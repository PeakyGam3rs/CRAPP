package com.svgs.framework.app;

import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;

/**
 * One live metric: the OBD command that reads it, how many bytes come back,
 * and the property gauges bind to.
 */
public class DataProperty {
    // final on purpose. gauges bind to this exact object, so it must never be
    // swapped out from under them.
    private final SimpleDoubleProperty data;
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

    /** Latest reading as a plain number, for saving to CSV. */
    public double currentValue() {
        return data.get();
    }

    /**
     * Stores a new reading. The poller calls this from its own thread, but anything
     * bound to this property lives on the FX thread, so the write is hopped over.
     */
    public void writeValue(double value) {
        if (Platform.isFxApplicationThread()) {
            data.set(value);
        } else {
            Platform.runLater(() -> data.set(value));
        }
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
