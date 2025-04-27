package ru.igor17.stochastic.mechanics.util;

import java.util.List;
import java.util.stream.IntStream;

import static ru.igor17.stochastic.mechanics.Main.N;

public class ArithmeticUtils {

    public static Double mean(final List<Double> vector) {
        return vector.stream().mapToDouble(v -> v).average().orElse(0d);
    }

    public static Double sd(final List<Double> vector) {
        Double mean = mean(vector);
        double sumSqrt = vector.stream().mapToDouble(v -> (v - mean)*(v - mean)).sum();
        return Math.sqrt(sumSqrt/N);
    }

    public static double[] mulVectorByComponents(final List<Double> vector1, final List<Double> vector2) {
        return IntStream.range(0, vector1.size())
                .mapToDouble(i -> vector1.get(i) * vector2.get(i))
                .toArray();
    }

}
