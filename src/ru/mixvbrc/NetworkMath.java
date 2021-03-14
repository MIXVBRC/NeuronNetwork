package ru.mixvbrc;

public class NetworkMath {

    public static double getRandom(double min, double max)
    {
        double result;

        do {
            result = Math.random() * ( max - min ) + min;
        }while (result == 0);

        return result;
    }

    // Sigmoid
    public static double sigmoid(double data)
    {
        return 1 / ( 1 + Math.exp(-data) );
    }
    public static double derivativeSigmoid(double data)
    {
        return data * ( 1 - data );
    }

    // Hyperbolic tangent
    public static double hyperbolicTangent(double data)
    {
        return ( Math.exp(data) - Math.exp(-data) ) / ( Math.exp(data) + Math.exp(-data) );
    }
    public static double derivativeHyperbolicTangent(double data)
    {
        return 1 - Math.pow(data, 2);
    }

    // ReLU
    public static double ReLU(double data)
    {
        return data >= 0 ? data : 0;
    }
    public static double derivativeReLU(double data)
    {
        return data >= 0 ? 1 : 0;
    }

    // Normalize
    public static double normalizeData(double data, double minData, double maxData, double minNormalize, double maxNormalize)
    {
        return ( ( data - minData ) * ( maxNormalize - minNormalize ) / ( maxData - minData ) ) + minNormalize;
    }
    public static double backNormalizeData(double data, double minData, double maxData, double minNormalize, double maxNormalize)
    {
        return ( ( ( data - minNormalize ) * ( maxData - minData ) ) / ( maxNormalize - minNormalize ) ) + minData;
    }
}