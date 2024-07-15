package com.thealgorithms.audiofilters;

/**
 * Is designed to perform a sequence of calculations using coefficients and previous
 * history values to produce an output value. It takes in a sample value and processes
 * it through the given coefficients, history values, and feedback loops to generate
 * a calculated value representing the current state of the system. The class provides
 * methods for setting coefficients and processing samples.
 */
public class IIRFilter {
    private final int order;
    private final double[] coeffsA;
    private final double[] coeffsB;
    private final double[] historyX;
    private final double[] historyY;

    public IIRFilter(int order) throws IllegalArgumentException {
        if (order < 1) {
            throw new IllegalArgumentException("order must be greater than zero");
        }

        
        this.order = order;
        coeffsA = new double[order + 1];
        coeffsB = new double[order + 1];

        // Sane defaults
        coeffsA[0] = 1.0;
        coeffsB[0] = 1.0;

        historyX = new double[order];
        historyY = new double[order];
    }

    
    /**
     * Sets the coefficients for a polynomial multiplication operation, verifying the
     * input sizes and ranges, and assigning them to corresponding arrays.
     * 
     * @param aCoeffs 0th to `order-1st` coefficients of the polynomial, which are used
     * to initialize the `coeffsA` array.
     * 
     * * `aCoeffs.length`: Must be equal to `order`.
     * * `aCoeffs[0]`: Must not be zero.
     * 
     * @param bCoeffs 2nd polynomial coefficients to be multiplied with the first
     * polynomial's coefficients.
     * 
     * * Length: `bCoeffs.length` must equal to `order`, which is a parameter passed to
     * the function.
     * * Values: The values of `bCoeffs` must be non-zero and non-negative.
     */
    public void setCoeffs(double[] aCoeffs, double[] bCoeffs) throws IllegalArgumentException {
        if (aCoeffs.length != order) {
            throw new IllegalArgumentException("aCoeffs must be of size " + order + ", got " + aCoeffs.length);
        }

        if (aCoeffs[0] == 0.0) {
            throw new IllegalArgumentException("aCoeffs.get(0) must not be zero");
        }

        if (bCoeffs.length != order) {
            throw new IllegalArgumentException("bCoeffs must be of size " + order + ", got " + bCoeffs.length);
        }

        for (int i = 0; i <= order; i++) {
            coeffsA[i] = aCoeffs[i];
            coeffsB[i] = bCoeffs[i];
        }
    }

  
    /**
     * Takes a sample value and processes it through a recurrent neural network (RNN)
     * with an order of `order`, computing the output value based on previous input values
     * stored in `historyX` and `historyY`. The function then feeds back the output value
     * to the beginning of the RNN chain, updating the input values.
     * 
     * @param sample input value that is processed by the function.
     * 
     * @returns a double value representing the processed sample.
     */
    public double process(double sample) {
        double result = 0.0;

        // Process
        for (int i = 1; i <= order; i++) {
            result += (coeffsB[i] * historyX[i - 1] - coeffsA[i] * historyY[i - 1]);
        }
        result = (result + coeffsB[0] * sample) / coeffsA[0];

        // Feedback
        for (int i = order - 1; i > 0; i--) {
            historyX[i] = historyX[i - 1];
            historyY[i] = historyY[i - 1];
        }

        historyX[0] = sample;
        historyY[0] = result;

        return result;
    }
}