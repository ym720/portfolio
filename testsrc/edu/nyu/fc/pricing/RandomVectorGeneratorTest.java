package edu.nyu.fc.pricing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Test functionality of random vector generator
 * 
 * @author Yourii Martiak
 *
 */
public class RandomVectorGeneratorTest {

    private static final int SIZE = 252;
    private static final double DEVIATION = 1.0;

    private IRandomVectorGenerator vectorGenerator;

    @Before
    public void setUp() {
        vectorGenerator = new RandomVectorGenerator(SIZE, DEVIATION);
    }

    /**
     * Test and verify that we were able to create a new instance of random
     * vector generator
     */
    @Test
    public void testRandomVectorGenerator() {
        assertNotNull("Check random vector initialization", vectorGenerator);
    }

    /**
     * Test and verify that array return by this random number generator created
     * successfully and is of the correct size
     */
    @Test
    public void testGetVector() {
        final double[] vector = vectorGenerator.getVector();
        assertNotNull("Check array creation", vector);
        assertEquals("Check array size", SIZE, vector.length);
        for (int i = 0; i < SIZE; i++) {
            System.out.println(String.format("%f", vector[i]));
        }
    }

}
