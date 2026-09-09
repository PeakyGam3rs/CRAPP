package com.svgs.framework.frontend;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import eu.hansolo.medusa.Gauge.SkinType;

public class PreferenceRegistry {
    // LinkedHashMap, not HashMap: the menu order and the save-file column order both
    // read off this, so it has to stay in the order written below (which matches
    // DataRegistry). A plain HashMap orders by hash and silently reshuffles both.
    public static final Map<String, Preference> registry = createRegistry();

    public static Map<String, String> createValueTitleMap() {
        Map<String, String> map = new LinkedHashMap<>();

        for (Map.Entry<String, Preference> entry : registry.entrySet()) {
            map.put(entry.getKey(), entry.getValue().getTitle());
        }

        return map;
    }

    /** Looks up a preference by value title, e.g. "boostValue". */
    public static Preference getPreference(String valueTitle) {
        Preference p = registry.get(valueTitle);
        if (p == null) {
            throw new IllegalArgumentException("unregistered value title: " + valueTitle);
        }
        return p;
    }

    /** Reverse lookup: display title ("Boost Pressure") back to value title ("boostValue"). */
    public static String getValueTitle(String gaugeTitle) {
        for (Map.Entry<String, Preference> entry : registry.entrySet()) {
            if (entry.getValue().getTitle().equals(gaugeTitle)) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("unregistered gauge title: " + gaugeTitle);
    }

    public static ArrayList<String> getPreferenceTitles() {
        ArrayList<String> result = new ArrayList<>();

        for (Preference p : registry.values()) {
            result.add(p.getTitle());
        }

        return result;
    }

    private static Map<String, Preference> createRegistry() {
        Map<String, Preference> map = new LinkedHashMap<>();

        map.put("boostValue", createBoost());
        map.put("revValue", createRev());
        map.put("trimValue", createTrim());
        map.put("fuelPressureValue", createFuelPressure());
        map.put("coolantValue", createCoolant());
        map.put("loadValue", createLoad());
        map.put("speedValue", createSpeed());
        map.put("throttleValue", createThrottle());
        map.put("timingValue", createTiming());

        return map;
    }

    private static Preference createBoost() {
        Preference p = new Preference();

        p.setTitle("Boost Pressure");
        p.setUnit("psi");
        p.setSkinType(SkinType.KPI);
        p.setMinValue(0);
        p.setMaxValue(15);
        p.setThreshold(8);
        p.setThresholdVisible(true);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createRev() {
        Preference p = new Preference();

        p.setSkinType(SkinType.HORIZONTAL);
        p.setTitle("RPMs");
        p.setUnit("rpm");
        p.setAngleRange(120);
        p.setMinValue(0);
        p.setMaxValue(7000);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createTrim() {
        Preference p = new Preference();

        p.setSkinType(SkinType.LINEAR);
        p.setTitle("Fuel Trim");
        p.setUnit("%");
        p.setMinValue(-50);
        p.setMaxValue(50);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createFuelPressure() {
        Preference p = new Preference();

        p.setSkinType(SkinType.LCD);
        p.setTitle("Fuel Pressure");
        p.setUnit("kPa");
        p.setMinValue(0);
        p.setMaxValue(765);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createCoolant() {
        Preference p = new Preference();

        p.setSkinType(SkinType.FLAT);
        p.setTitle("Coolant Temperature");
        p.setUnit("\u00B0C");
        p.setMinValue(0);
        p.setMaxValue(120);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createLoad() {
        Preference p = new Preference();

        p.setSkinType(SkinType.MODERN);
        p.setTitle("Engine Load");
        p.setUnit("%");
        p.setMinValue(0);
        p.setMaxValue(100);
        p.setValueVisible(true);

        return p;
    }

    private static Preference createSpeed() {
        Preference p = new Preference();

        p.setSkinType(SkinType.HORIZONTAL);
        p.setTitle("Speed");
        p.setUnit("km/h");
        p.setAngleRange(120);
        p.setMinValue(0);
        p.setMaxValue(160);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createThrottle() {
        Preference p = new Preference();

        p.setSkinType(SkinType.LINEAR);
        p.setTitle("Throttle Position");
        p.setUnit("%");
        p.setMinValue(0);
        p.setMaxValue(100);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createTiming() {
        Preference p = new Preference();

        p.setSkinType(SkinType.SIMPLE_DIGITAL);
        p.setTitle("Timing Advance");
        p.setUnit("\u00B0");
        p.setMinValue(-100);
        p.setMaxValue(100);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

}
