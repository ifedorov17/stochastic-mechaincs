package ru.igor17.stochastic.mechanics.util;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static ru.igor17.stochastic.mechanics.Main.N;
import static ru.igor17.stochastic.mechanics.util.ConvertUtils.toVectorColumnMatrix;

public class ArithmeticUtils {

    /**
     * Среднее арифметическое
     */
    public static Double mean(final List<Double> vector) {
        return vector.stream().mapToDouble(v -> v).average().orElse(0d);
    }

    /**
     * Среднеквадратичное отклонение
     */
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

    public static RealVector mulMatrixByVector(final RealMatrix matrix, final RealVector vector) {
        RealMatrix columnMatrix = toVectorColumnMatrix(vector);
        return matrix.multiply(columnMatrix).getColumnVector(0);
    }

    public static List<Double> deltasBeta(double studentQuantile, double s2_ost, final RealMatrix C) {
        List<Double> deltaVector = new ArrayList<>();
        for (int i = 0; i < C.getRowDimension(); i++) {
            deltaVector.add(Math.sqrt(s2_ost * C.getEntry(i, i)) * studentQuantile);
        }
        return deltaVector;
    }

}
