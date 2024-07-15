package com.thealgorithms.audiofilters;

/**
 * Is a filtering mechanism that takes in a sample value and processes it through a
 * series of coefficients, producing an output value. The class has several methods
 * for setting the coefficients and processing samples, with the `process()` method
 * being the primary method for filtering input values.
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
     * Sets the coefficients of a and b to those passed in, checks if they are correct
     * lengths, and then assigns them to the corresponding array indices.
     * 
     * @param aCoeffs 1st polynomial coefficients to be multiplied with the input signal.
     * 
     * * Length: `aCoeffs.length == order`, where `order` is a positive integer indicating
     * the degree of the polynomial.
     * * Non-zero value at index 0: `aCoeffs[0] != 0.0`.
     * 
     * @param bCoeffs 2nd set of coefficients that are multiplied with the input signal
     * to produce the output signal in the given linear transformation.
     * 
     * * `bCoeffs` has length `order`.
     * * All elements of `bCoeffs` are non-zero.
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
     * Takes a sample value and processes it through an iterative feedback loop, using
     * coefficients to compute the output value based on previous values of both inputs
     * and outputs.
     * 
     * @param sample 0-to-1 value that is processed by the function.
     * 
     * @returns a calculated value for the next time step in a feedback control system.
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