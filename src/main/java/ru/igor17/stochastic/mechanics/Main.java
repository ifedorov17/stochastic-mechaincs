package ru.igor17.stochastic.mechanics;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.List;
import java.util.stream.Stream;

import static ru.igor17.stochastic.mechanics.util.ConvertUtils.toDoubleArray;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.print;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.printHeader;

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

        RealMatrix F = new Array2DRowRealMatrix(6,9);

        double[] row0 = {1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d};
        F.setRow(0, row0);
        F.setRow(1, toDoubleArray(x1));
        F.setRow(2, toDoubleArray(x2));

        print("F", F);

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
