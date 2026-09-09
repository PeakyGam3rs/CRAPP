package com.svgs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.hansolo.medusa.Gauge;

/**
 * The gauges the user has added. Lives outside the controllers because every
 * scene swap builds a brand new PrimaryController, and the gauges have to survive that.
 */
public class GaugeList {
    private static final List<Gauge> list = new ArrayList<>();

    public static void addGauge(Gauge g) {
        list.add(g);
    }

    public static List<Gauge> getGauges() {
        return Collections.unmodifiableList(list);
    }

    public static boolean isEmpty() {
        return list.isEmpty();
    }
}
