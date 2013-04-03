package edu.nyu.fc.pricing;

/**
 * Implementation of antithetic random number generator using decorator pattern.
 * This implementation takes existing instance of {@link IRandomVectorGenerator}
 * and creates its local copy with a set of values that negate the original
 * values
 * 
 * @author Yourii Martiak
 *
 */
public class AntiTheticRandomVectorGenerator implements IRandomVectorGenerator {
    /**
     * Keeps the count of number of invocations of this method
     */
    private long count = 0;
    
    /**
     * Local "negated" copy of random vector generator values
     */
    private final double[] vector;    
    
    /**
     * Original random vector generator
     */
    private final IRandomVectorGenerator vectorGenerator;
    
    /**
     * Creates a new instance of random vector generator using decorator
     * pattern to implement antithetic values
     * 
     * @param vectorGenerator original vector generator
     */
    public AntiTheticRandomVectorGenerator(IRandomVectorGenerator vectorGenerator) {
        this.vectorGenerator = vectorGenerator;
        this.vector = new double[vectorGenerator.getVector().length];        
    }
    
    /**
     * Initialize local copy with "negated" values
     */
    private void initialize() {
        double[] originalVector = vectorGenerator.getVector();
        int size = originalVector.length;
        for (int i = 0; i < size; i++) {
            vector[i] = -originalVector[i];
        }
    }

    @Override
    public double[] getVector() {
        initialize();
        count++;
        if (count % 2 == 0) {
            return vector;
        } else {
            return vectorGenerator.getVector();
        }
    }

}
