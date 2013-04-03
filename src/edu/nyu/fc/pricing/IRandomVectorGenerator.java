package edu.nyu.fc.pricing;

/**
 * Common interface to be used by implementing random number generators. Typically
 * one would follow some distribution type (i.e. standard normal distribution)
 * 
 * @author Yourii Martiak
 *
 */
public interface IRandomVectorGenerator {
    
    /**
     * Obtain list of random values for a given distribution implementation
     * 
     * @return list of random numbers
     */
    public double[] getVector();

}
