package com.svgs.framework.frontend;

import java.util.ArrayList;
import java.util.HashMap;

import eu.hansolo.medusa.Gauge.SkinType;

public class PreferenceRegistry {
    public static final HashMap<String, Preference> registry = createRegistry();

    public static HashMap<String, String> createValueTitleMap() {
      HashMap<String, String> map = new HashMap<>();

      for (String key : registry.keySet()) {
        map.put(key, registry.get(key).getTitle());
      }

      return map;
    }

    public static Preference getPreference(String valueTitle) {
        for (String key : registry.keySet()) {
            if (key.equals(valueTitle)) {
                return registry.get(valueTitle);
            }
        }
        throw new IllegalArgumentException("unregistered value title");
    }

    public static String getValueTitle(String gaugeTitle) {
        for (String key : registry.keySet()) {
            if (registry.get(key).getTitle().equals(gaugeTitle)) {
                return key;
            }
        }
        return "unregistered gauge title";
    }

    public static ArrayList<String> getPreferenceTitles() {
        ArrayList<String> result = new ArrayList<>();

        for (Preference p : registry.values()) {
            result.add(p.getTitle());
        }

        return result;
    }
    
    private static HashMap<String, Preference> createRegistry() {
        HashMap<String, Preference> map = new HashMap<>();

        map.put("boostValue", createBoost());
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
        p.setSkinType(SkinType.KPI);
        p.setMinValue(0);
        p.setMaxValue(15);
        p.setThreshold(8);
        p.setThresholdVisible(true);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createTrim() {
        Preference p = new Preference();

        p.setSkinType(SkinType.LINEAR);
        p.setTitle("Fuel Trim");
        p.setMinValue(-50);
        p.setMaxValue(50);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createFuelPressure() {
        Preference p = new Preference();

        p.setSkinType(SkinType.LCD);
        p.setTitle("Fuel Pressure");
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createCoolant() {
        Preference p = new Preference();

        p.setSkinType(SkinType.FLAT);
        p.setTitle("Coolant Temperature");
        p.setMinValue(0);
        p.setMaxValue(120);
        p.setIsAnimated(true);

        return p;
    }

    private static Preference createLoad() {
        Preference p = new Preference();

        p.setSkinType(SkinType.MODERN);
        p.setTitle("Engine Load");
        p.setMinValue(0);
        p.setMaxValue(100);
        p.setValueVisible(true);

        return p;
    }

    private static Preference createSpeed() {
        Preference p = new Preference();

        p.setSkinType(SkinType.HORIZONTAL);
        p.setTitle("Speed");
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
        p.setMinValue(-100);
        p.setMaxValue(100);
        p.setValueVisible(true);
        p.setIsAnimated(true);

        return p;
    }

}