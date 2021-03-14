package ru.mixvbrc;

public class Main {

    public static void main(String[] args) {

        //testNetwork();
    }

    private double getState(double[] data)
    {
        double result = 0;

        for (double d : data)
        {

        }

        return result;
    }

    private static void testNetwork()
    {
        Network network = new Network(new int[]{2, 12, 12, 1}, true);

        network.setMinData(0);
        network.setMaxData(10);
        network.setMinNormalize(0.1);
        network.setMaxNormalize(0.9);

        network.setSpeed(1);

        double[][] inputs = new double[][]{
                {10, 2},
                {5, 2},
                {2, 2},
                {10, 5},
                {5, 5},
                {2, 5},
                {10, 10},
                {5, 10},
                {2, 10},
        };

        double[][] answers = new double[][] {
                {5},
                {2.5},
                {0},
                {2},
                {0},
                {0.4},
                {0},
                {0.5},
                {0.2},
        };



        network.learn(inputs, answers, 100000);



        network.setInputsData(new double[]{10, 2});
        for( double d : network.getOutputsData())
            System.out.println(d);

    }

    private static double normalizeCustom(double data, double min, double max, double d1, double d2)
    {
        return ( ( ( data - min ) * ( d2 - d1 ) ) / ( max - min ) ) + d1;
    }

    private static double backNormalizeCustom(double data, double min, double max, double d1, double d2)
    {
        return ( ( ( data - d1 ) * ( max - min ) ) / ( d2 - d1 ) ) + min;
    }

    private static double getRandom(double min, double max)
    {
        double result;

        do {
            result = Math.random() * ( max - min ) + min;
        }while (result == 0);

        return result;
    }

    private static double getDistance(double[] a, double[] b)
    {
        return Math.abs(Math.sqrt(Math.pow(a[0]-b[0],2)+Math.pow(a[1]-b[1],2)+Math.pow(a[2]-b[2],2)));
    }
}
