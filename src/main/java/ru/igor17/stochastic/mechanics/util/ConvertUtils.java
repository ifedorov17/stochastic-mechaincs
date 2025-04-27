package ru.igor17.stochastic.mechanics.util;

import java.util.List;

public class ConvertUtils {

    public static double[] toDoubleArray(List<Double> doubleList) {
        return doubleList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

}
