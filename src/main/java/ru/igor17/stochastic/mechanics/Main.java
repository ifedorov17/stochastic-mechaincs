package ru.igor17.stochastic.mechanics;

import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static final int N = 9;
    private static final int k = 6;

    public static void main(String[] args) {

        printHeader("Начальные условия");
        final List<Double> t = Stream.of(6.0, 6.0, 1.0, 1.0, 3.5, 3.5, 6.0, 3.5, 1.0).toList();
        final List<Double> T = Stream.of(145.0, 70.0, 145.0, 70.0, 107.5, 145.0, 107.5, 70.0, 107.5).toList();
        final List<Double> y = Stream.of(72.0, 63.0, 57.0, 49.0, 61., 67., 64., 56., 52.).toList();

        print("N", N);
        print("K", k);
        print("t", t);
        print("T", T);
        print("y", y);

        printHeader("Переход к безразмерным переменным");
        Double mean_t = mean(t);
        Double mean_T = mean(T);
        Double mean_y = mean(y);

        print("Среднее t", mean_t);
        print("Среднее T", mean_T);
        print("Среднее y", mean_y);

        Double sd_t = sd(t);
        Double sd_T = sd(T);
        Double sd_y = sd(y);

        print("Среднеквадратичное отклонение t", sd_t);
        print("Среднеквадратичное отклонение T", sd_T);
        print("Среднеквадратичное отклонение y", sd_y);

        var x1 = t.stream().map(ti -> (ti - mean_t)/sd_t).toList();
        var x2 = T.stream().map(Ti -> (Ti - mean_T)/sd_T).toList();
        var yy = y.stream().map(yi -> (yi - mean_y)/sd_y).toList();

        print("Безразмерное t (x1)", x1);
        print("Безразмерное T (x2)", x2);
        print("Безразмерное y", yy);

        //Модель: y = beta0 + beta1*x1 + beta2*x2 + beta3*x1*x2 + beta4*x1^2 + beta5*x2^2 + eps

        printHeader("Матрица задачи F");

    }


    private static void print(String explanation, final Object arg) {
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
            default -> throw new UnsupportedOperationException("Unknown type");
        }
    }

    private static void printHeader(String header) {
        System.out.printf("%n%n---%s---%n%n", header);
    }

    private static Double mean(final List<Double> vector) {
        return vector.stream().mapToDouble(v -> v).average().orElse(0d);
    }

    private static Double sd(final List<Double> vector) {
        Double mean = mean(vector);
        double sumSqrt = vector.stream().mapToDouble(v -> (v - mean)*(v - mean)).sum();
        return Math.sqrt(sumSqrt/N);
    }

}
