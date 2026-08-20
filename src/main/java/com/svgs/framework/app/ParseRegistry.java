package com.svgs.framework.app;

import java.util.Map;

public class ParseRegistry {
    @FunctionalInterface
    public interface IntBinaryToDoubleOperator {
        double apply(int a, int b);
    }

    private static final Map<String, IntBinaryToDoubleOperator> OPERATIONS = Map.of(
        "boostValue", (a, b) -> (a - 14.7346)/6.895, //translated to boost psi
        "revValue", (a, b) -> (256*a + b)/4, 
        "trimValue", (a, b) -> (100.0/128.0) * a - 100,
        "fuelPressureValue", (a, b) -> a * 3.0,
        "coolantValue", (a, b) -> a-40.0,
        "loadValue", (a, b) -> 100.0/255.0 * a,
        "speedValue", (a, b) -> a,
        "throttleValue", (a, b) -> 100.0/255.0 * a,
        "timingValue", (a,b) -> ((a/2.0)-64)
    );

    public static double execute(String title, int a, int b) {
        IntBinaryToDoubleOperator op = OPERATIONS.get(title);
        if (op == null) {
            throw new IllegalArgumentException("No operation registered for '" + title + "'");
        }
        return op.apply(a, b);
    }
}
