package edu.nyu.fc.pricing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

/**
 * Test functionality of stock path
 * 
 * @author Yourii Martiak
 * 
 */
public class StockPathTest {

    private static final int SIZE = 5;
    private static final double DEVIATION = 1.0;

    private final double price = 152.35;
    private final double rate = 0.0001;
    private final double volatility = 0.01;
    private double[] vector;
    private IStockPath stockPath;

    @Before
    public void setUp() throws Exception {
        vector = new RandomVectorGenerator(SIZE, DEVIATION).getVector();
        stockPath = new StockPath(price, rate, volatility, vector);
    }

    /**
     * Test and verify that an instance of stock path created successfully
     */
    @Test
    public void testStockPath() {
        assertNotNull(stockPath);
    }

    /**
     * Test and verify that the list of price points is of correct size
     */
    @Test
    public void testGetPrices_Size() {
        final List<IPricePoint> list = stockPath.getPrices();
        assertEquals("Check correct size", SIZE, list.size());
    }

    /**
     * Check the difference between first and last time stamp created for the
     * price points
     */
    @Test
    public void testGetPrices_Dates() {
        final List<IPricePoint> list = stockPath.getPrices();
        final IPricePoint firstPricePoint = list.get(0);
        final IPricePoint lastPricePoint = list.get(SIZE - 1);
        assertEquals("Check correct dates for first and last price points",
                lastPricePoint.getDateTime(), firstPricePoint.getDateTime()
                        .plusDays(SIZE - 1));
        System.out.println(list);
    }

    /**
     * Check that the price points are listed in ascending sorted order
     * according to time stamp for date they are created
     */
    @Test
    public void testGetPrices_SortedOrder() {
        final List<IPricePoint> list = stockPath.getPrices();
        DateTime expectedDate = list.get(0).getDateTime();
        for (final IPricePoint nextPricePoint : list) {
            assertEquals(expectedDate, nextPricePoint.getDateTime());
            expectedDate = expectedDate.plusDays(1);
        }
    }
}
