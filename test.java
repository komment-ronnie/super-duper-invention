package com.thealgorithms.audiofilters;

/**
 * Processes audio signals using a IIR filter structure. It takes in an integer
 * parameter representing the filter's order and allows for setting the coefficients
 * A and B through separate methods. The process method applies the filter to a given
 * sample, feeding back the output to the input, and returns the processed result.
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
     * Sets the coefficients for a polynomial transformation between two arrays of doubles,
     * verifying their lengths and values before updating the internal coefficients array.
     * 
     * @param aCoeffs 1st polynomial coefficients to be multiplied with the other polynomial
     * `bCoeffs`.
     * 
     * * Length: `aCoeffs.length` must be equal to `order`, which is a constant in the function.
     * * Non-zero value at index 0: The element at index 0 of `aCoeffs` cannot be zero.
     * 
     * @param bCoeffs 2nd polynomial's coefficients, which must have the same length as
     * the `aCoeffs` array and be non-zero.
     * 
     * * `bCoeffs` is a double array of length `order`.
     * * The values in `bCoeffs` must be non-zero.
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
     * Processes a given input `sample` using a recurrent neural network (RNN) architecture,
     * computing the output based on previous inputs and coefficients.
     * 
     * @param sample initial value of the system being modeled, which is fed into the
     * feedback loop and influenced by the processing done in the function.
     * 
     * @returns a double value representing the result of the recursive calculation.
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