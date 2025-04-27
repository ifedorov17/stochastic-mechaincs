package ru.igor17.stochastic.mechanics.util;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.List;

public class ConvertUtils {

    public static double[] toDoubleArray(List<Double> doubleList) {
        return doubleList.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public static RealMatrix toVectorColumnMatrix(RealVector vector) {
        final int dim = vector.getDimension();
        double[][] data = new double[dim][1];
        for (int i = 0; i < dim; ++i) {
            data[i][0] = vector.getEntry(i);
        }
        return new Array2DRowRealMatrix(data, false);
    }

    public static RealVector toRealVector(List<Double> vector) {
        return new ArrayRealVector(toDoubleArray(vector));
    }

    public static List<Double> realVectorToList(RealVector realVector) {
        var result = new ArrayList<Double>();
        for (int i = 0; i < realVector.getDimension(); ++i) {
            result.add(realVector.getEntry(i));
        }
        return result;
    }

}
