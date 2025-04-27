package ru.igor17.stochastic.mechanics.util;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.List;

public class PrintUtils {

    private static final String REAL_FORMAT = "%10.5f";

    public static void print(String explanation, final Object arg) {
        switch (arg) {
            case Double d -> System.out.printf("%s: %.5f%n", explanation, d);
            case Integer i -> System.out.printf("%s: %d%n", explanation, i);
            case List l -> {
                StringBuilder result = new StringBuilder("[");
                for (Object o : l) {
                    result.append(String.format(REAL_FORMAT, (Double) o));
                }
                result.append("]");
                System.out.printf("%s: %s%n", explanation, result);
            }
            case RealMatrix m -> {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < m.getRowDimension(); i++) {
                    for (int j = 0; j < m.getColumnDimension(); j++) {
                        result.append(String.format(REAL_FORMAT, m.getEntry(i, j)));
                    }
                    result.append("\n");
                }
                System.out.printf("Матрица %s:%n%s", explanation, result);
            }
            case RealVector v -> {
                StringBuilder result = new StringBuilder("[");
                for (int i = 0; i < v.getDimension(); i++) {
                    result.append(String.format(REAL_FORMAT, v.getEntry(i)));
                }
                result.append("]");
                System.out.printf("%s: %s", explanation, result);
            }
            default -> throw new UnsupportedOperationException("Unknown type");
        }
    }

    public static void printHeader(String header) {
        System.out.printf("%n%n---%s---%n", header);
    }

    public static void checkStudentAndPrintResult(List<Double> results, Double quantile) {
        for (int i = 0; i < results.size(); i++) {
            if (results.get(i) >= quantile) {
                System.out.printf("W2_%d > t_student (%.5f > %.5f), beta_%d значим %n", i, results.get(i), quantile, i);
            } else {
                System.out.printf("W2_%d < t_student (%.5f < %.5f), beta_%d не значим %n", i, results.get(i), quantile, i);
            }
        }
    }

}
