package ru.igor17.stochastic.mechanics;

import java.util.List;
import java.util.stream.Stream;

public class Main {

    private static final int N = 9;
    private static final int k = 6;

    public static void main(String[] args) {
        System.out.print("Mechanics!!!\n\n");

        System.out.println("\nНачальные условия");

        //Начальные условия
        final List<Double> t = Stream.of(6.0, 6.0, 1.0, 1.0, 3.5, 3.5, 6.0, 3.5, 1.0).toList();
        final List<Double> T = Stream.of(145.0, 70.0, 145.0, 70.0, 107.5, 145.0, 107.5, 70.0, 107.5).toList();
        final List<Double> y = Stream.of(72.0, 63.0, 57.0, 49.0, 61., 67., 64., 56., 52.).toList();

        print("N", N);
        print("K", k);
        print("t", t);
        print("T", T);
        print("y", y);

        //Переход к безразмерным переменным
        System.out.println("\nПереход к безразмерным переменным");
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

    private static Double mean(final List<Double> vector) {
        return vector.stream().mapToDouble(v -> v).average().orElse(0d);
    }

    private static Double sd(final List<Double> vector) {
        Double mean = mean(vector);
        double sumSqrt = vector.stream().mapToDouble(v -> (v - mean)*(v - mean)).sum();
        return Math.sqrt(sumSqrt/N);
    }

}
