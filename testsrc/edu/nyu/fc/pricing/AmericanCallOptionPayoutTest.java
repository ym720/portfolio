package edu.nyu.fc.pricing;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class AmericanCallOptionPayoutTest {
    
    private static final int SIZE = 252;
    private static final double DEVIATION = 1.0;
    private static final double PRICE = 152.35;
    private static final double RATE = 0.0001;
    private static final double SIGMA = 0.01;
    private static final double STRIKE_PRICE = 165.0;
    
    private IRandomVectorGenerator vectorGenerator;
    private IStockPath stockPath;
    private IPayout payout;
    
    @Before
    public void setUp() {
        vectorGenerator = new RandomVectorGenerator(SIZE, DEVIATION);
        stockPath = new StockPath(PRICE, RATE, SIGMA, vectorGenerator.getVector());
        payout = new AmericanCallOptionPayout(STRIKE_PRICE);
    }

    /**
     * Test and verify we are able to create an instance of payout class
     */
    @Test
    public void test() {
        assertNotNull(payout);
        System.out.println("Average payout is " + payout.getPayout(stockPath));
    }

}
