package edu.nyu.fc.pricing;

import org.apache.commons.math3.distribution.NormalDistribution;

/**
 * This class is used to generate a vector of random numbers that follow rules
 * of standard normal distribution
 * 
 * @author Yourii Martiak
 *
 */
public class RandomVectorGenerator implements IRandomVectorGenerator {
    
    private final double mean;
    private final double deviation;
    
    private final double[] vector;
    
    /**
     * Creates new random vector generator with given size and volatility
     * 
     * @param size size of the vector
     * @param deviation used as standard deviation for generating random numbers
     */
    public RandomVectorGenerator(int size, double mean, double deviation) {
        this.mean = mean;
        this.deviation = deviation;
        this.vector = new double[size];
    }
    
    public RandomVectorGenerator(int size, double deviation) {
        this(size, 0.0d, deviation);
    }
    
    /**
     * Initialize vector with random numbers
     * 
     * @param normalDistribution
     */
    private void initialize(NormalDistribution normalDistribution) {
        final int size = vector.length;
        for (int i = 0; i < size; i++) {
            vector[i] = normalDistribution.sample();
        }
    }

    @Override
    public double[] getVector() {
        initialize(new NormalDistribution(mean, deviation));
        return vector;
    }

}
