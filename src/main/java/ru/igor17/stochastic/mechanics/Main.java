package ru.igor17.stochastic.mechanics;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static ru.igor17.stochastic.mechanics.util.ArithmeticUtils.deltas;
import static ru.igor17.stochastic.mechanics.util.ArithmeticUtils.mean;
import static ru.igor17.stochastic.mechanics.util.ArithmeticUtils.mulMatrixByVector;
import static ru.igor17.stochastic.mechanics.util.ArithmeticUtils.mulVectorByComponents;
import static ru.igor17.stochastic.mechanics.util.ArithmeticUtils.sd;
import static ru.igor17.stochastic.mechanics.util.ConvertUtils.realVectorToList;
import static ru.igor17.stochastic.mechanics.util.ConvertUtils.toDoubleArray;
import static ru.igor17.stochastic.mechanics.util.ConvertUtils.toRealVector;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.checkStudentAndPrintResult;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.print;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.printConfidenceIntervalBeta;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.printConfidenceIntervalY;
import static ru.igor17.stochastic.mechanics.util.PrintUtils.printHeader;

public class Main {

    /**
     * Размерность векторов параметров
     */
    public static final int N = 9;

    /**
     * Количество коэффициентов в постулируемой модели
     */
    public static final int k = 6;

    /**
     * Количество дополнительных измерений
     */
    public static final int n = 6;

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

        printHeader("РЕГРЕССИЯ");

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

        printHeader("Матрица задачи");

        RealMatrix F = new Array2DRowRealMatrix(6,9);

        double[] row0 = {1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d};
        F.setRow(0, row0);
        F.setRow(1, toDoubleArray(x1));
        F.setRow(2, toDoubleArray(x2));
        F.setRow(3, mulVectorByComponents(x1, x2));
        F.setRow(4, mulVectorByComponents(x1, x1));
        F.setRow(5, mulVectorByComponents(x2, x2));

        print("F", F);

        printHeader("Информационная матрица");
        RealMatrix G = F.multiply(F.transpose());
        print("G", G);

        printHeader("Матрица ошибок");
        RealMatrix C = MatrixUtils.inverse(G);
        print("C", C);

        printHeader("Вектор оценок");
        RealMatrix CF = C.multiply(F);
        RealVector betaEstimate = mulMatrixByVector(CF, toRealVector(yy));

        print("betaEstimate", betaEstimate);


        printHeader("ПРОВЕРКА МОДЕЛИ НА АДЕКВАТНОСТЬ (gamma = 0.95");

        printHeader("Оценка вектора откликов");
        RealVector yEstimate = mulMatrixByVector(F.transpose(), betaEstimate);
        print("y", yEstimate);

        printHeader("Построение статистики");
        var yEstList = realVectorToList(yEstimate);
        double Q_ost = IntStream.range(0, yEstList.size())
                .mapToDouble(i -> (yEstList.get(i) - yy.get(i))*(yEstList.get(i) - yy.get(i))).sum();
        print("Остаточная сумма квадратов", Q_ost);
        double s2_ost = Q_ost/(N-k);
        print("Остаточная выборочная дисперсия", s2_ost);

        printHeader("Дополнительные измерения");
        final List<Double> yDop = Stream.of(61.5, 60.0, 61.0, 63.1, 60.5, 63., 61.5, 59.9, 61.2, 62.).toList();
        print("Доп. измерения yDop", yDop);

        printHeader("Нормирование измерений");
        var mean_yDop = mean(yDop);
        print("Среднее yDop", mean_yDop);
        var sd_yDop = Math.sqrt(sd(yDop));
        print("СКО yDop", sd_yDop);

        List<Double> yNorm = new ArrayList<>();
        IntStream.range(0, yDop.size()).forEach(i -> yNorm.add((yDop.get(i) - mean_yDop)/sd_yDop));
        print("Нормированные измерения", yNorm);

        var mean_yNorm = mean(yNorm);
        double sum = 0.;
        for (Double aDouble : yNorm) {
            sum += (aDouble - mean_yNorm) * (aDouble - mean_yNorm);
        }
        var s2 = sum/(n - 1);
        print("Выборочная дисперсия s2", s2);

        printHeader("Статистика");
        double W = s2_ost/s2;
        print("Статистика W", W);

        double quantileFisher = 3.8625483576247643;
        print("Квантиль Фишера", quantileFisher);

        if (W < quantileFisher) {
            System.out.println("Гипотеза принимается, W < t_fisher. Модель адекватна " + "(" + W + " < " + quantileFisher + ")");
        } else {
            System.out.println("Гипотеза отклоняется, W > t_fisher (" + W + " > " + quantileFisher + ")");
        }

        printHeader("ПРОВЕРКА ЗНАЧИМОСТИ КОЭФФИЦИЕНТОВ РЕГРЕССИИ (gamma = 0.95)");
        List<Double> W2 = new ArrayList<>();
        for (int i = 0; i < betaEstimate.getDimension(); i++) {
            W2.add(Math.abs(betaEstimate.getEntry(i))/(Math.sqrt(s2_ost*C.getEntry(i,i))));
        }
        print("Статистика W2", W2);
        var quantileStudent = 3.182446305284263;
        print("Квантиль Стьюдента (0.975)", quantileStudent);
        checkStudentAndPrintResult(W2, quantileStudent);

        printHeader("ПРОВЕРКА ЗНАЧИМОСТИ КОЭФФИЦИЕНТОВ РЕГРЕССИИ (gamma = 0.8)");
        var quantileStudent08 = 1.637744357215907;
        print("Квантиль Стьюдента", quantileStudent08);
        checkStudentAndPrintResult(W2, quantileStudent08);

        printHeader("ДОВЕРИТЕЛЬНЫЕ ИНТЕРВАЛЫ для коэффициентов (gamma = 0.95)");
        var deltaBeta095 = deltas(quantileStudent, s2_ost, C);
        printConfidenceIntervalBeta(realVectorToList(betaEstimate), deltaBeta095);

        printHeader("ДОВЕРИТЕЛЬНЫЕ ИНТЕРВАЛЫ для коэффициентов (gamma = 0.8)");
        var deltaBeta08 = deltas(quantileStudent08, s2_ost, C);
        printConfidenceIntervalBeta(realVectorToList(betaEstimate), deltaBeta08);


        var A = F.transpose().multiply(C).multiply(F);
        printHeader("ДОВЕРИТЕЛЬНЫЕ ИНТЕРВАЛЫ для отклика (gamma = 0.95)");
        var deltaY095 = deltas(quantileStudent, s2_ost, A);
        printConfidenceIntervalY(realVectorToList(yEstimate), deltaY095);

        printHeader("ДОВЕРИТЕЛЬНЫЕ ИНТЕРВАЛЫ для отклика (gamma = 0.8)");
        var deltaY08 = deltas(quantileStudent08, s2_ost, A);
        printConfidenceIntervalY(realVectorToList(yEstimate), deltaY08);

    }

}
