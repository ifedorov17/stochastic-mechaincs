package ru.igor17.stochastic.mechanics.util;

import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;

public class PrintUtils {

    public static void print(String explanation, final Object arg) {
        switch (arg) {
            case Double d -> System.out.printf("%s: %.5f%n", explanation, d);
            case Integer i -> System.out.printf("%s: %d%n", explanation, i);
            case List l -> {
                StringBuilder result = new StringBuilder("[");
                for (int i = 0; i < l.size(); i++) {
                    result.append(String.format("%.5f", (Double) l.get(i)));
                    if (i < l.size() - 1) result.append(", ");
                }
                result.append("]");
                System.out.printf("%s: %s%n", explanation, result);
            }
            case RealMatrix m -> {
                StringBuilder result = new StringBuilder();
                for (int i = 0; i < m.getRowDimension(); i++) {
                    for (int j = 0; j < m.getColumnDimension(); j++) {
                        result.append(String.format("%10.5f ", m.getEntry(i, j)));
                    }
                    result.append("\n");
                }
                System.out.printf("Матрица %s:%n%s%n", explanation, result);
            }
            default -> throw new UnsupportedOperationException("Unknown type");
        }
    }

    public static void printHeader(String header) {
        System.out.printf("%n%n---%s---%n%n", header);
    }

}
