package com.thealgorithms.audiofilters;

/**
 * Is designed to process audio signals using a cascade of two filters, with the first
 * filter having a feedback loop and the second filter being a low-pass filter. The
 * class has a constructor that takes an integer parameter representing the order of
 * the filters, and methods for setting the coefficients of the filters and processing
 * audio samples. The `process()` method processes an audio sample by applying the
 * two filters in sequence, using the feedback loop to store the previous output
 * values and apply them to the next filter stage.
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
     * Sets coefficients for a polynomial of order at most `order`. It checks the lengths
     * of input arrays `aCoeffs` and `bCoeffs`, and ensures that all non-zero elements
     * in `aCoeffs` are not equal to zero. The function then assigns the values of `aCoeffs`
     * and `bCoeffs` to corresponding coefficients in an array `coeffsA`.
     * 
     * @param aCoeffs 1D array of coefficients for the polynomial used in the multiplication
     * operation.
     * 
     * * Length: `aCoeffs.length` must be equal to `order`.
     * * Non-zero value: The first element of `aCoeffs`, `aCoeffs.get(0)`, cannot be zero.
     * 
     * @param bCoeffs 2nd polynomial's coefficients, which must have the same length as
     * the `aCoeffs` parameter and be non-zero.
     * 
     * * Length: `bCoeffs.length` must equal to the order `order`.
     * * All elements of `bCoeffs` must have a non-zero value.
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
     * Takes a input `sample` and performs a sequence of calculations using coefficients,
     * previous history values, and feedback loops to produce an output value.
     * 
     * @param sample initial value of the system's state that is processed by the function.
     * 
     * @returns a calculated value representing the current state of the system.
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