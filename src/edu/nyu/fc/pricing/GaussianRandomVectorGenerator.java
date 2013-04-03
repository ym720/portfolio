package edu.nyu.fc.pricing;

import java.util.Random;

/**
 * Random number generator implementation of Gaussian normal distribution
 * 
 * @author Yourii Martiak
 * 
 */
public class GaussianRandomVectorGenerator implements IRandomVectorGenerator {

    /**
     * Array to store random values for this generator
     */
    private final double[] vector;

    /**
     * Creates new instance of gaussian random number generator for a given size
     * 
     * @param size
     */
    public GaussianRandomVectorGenerator(final int size) {
        vector = new double[size];
    }

    /**
     * Initialize vector with random values according to gaussian normal
     * distribution
     * 
     * @param random
     *            {@link java.util.Random} used to generate random numbers as
     *            per guassian normal distribution
     */
    private void initialize(final Random random) {
        final int size = vector.length;
        for (int i = 0; i < size; i++) {
            vector[i] = random.nextGaussian();
        }
    }

    @Override
    public double[] getVector() {
        initialize(new Random());
        return vector;
    }

}
