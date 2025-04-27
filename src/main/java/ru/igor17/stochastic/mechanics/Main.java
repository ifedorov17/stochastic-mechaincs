package ru.igor17.stochastic.mechanics;

import java.util.List;
import java.util.stream.Stream;

public class Main {

    public static void main(String[] args) {
        System.out.print("Mechanics!!!\n\n");

        //Начальные условия
        int N = 9;
        int k = 6;
        final List<Double> t = Stream.of(6.0, 6.0, 1.0, 1.0, 3.5, 3.5, 6.0, 3.5, 1.0).toList();
        final List<Double> T = Stream.of(145.0, 70.0, 145.0, 70.0, 107.5, 145.0, 107.5, 70.0, 107.5).toList();
        final List<Double> y = Stream.of(72.0, 63.0, 57.0, 49.0, 61., 67., 64., 56., 52.).toList();

        System.out.println("N: " + N);
        System.out.println("K: " + k);

        vectorPrint("t", t);
        vectorPrint("T", T);
        vectorPrint("y", y);

        //Переход к безразмерным переменным


    }

    private static void vectorPrint(String explanation, final List<Double> vector) {
        System.out.printf("%s: %s%n", explanation, vector.toString());
    }

}
