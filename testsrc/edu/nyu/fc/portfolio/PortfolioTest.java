package edu.nyu.fc.portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.fc.position.IPosition;

/**
 * JUnit test class to verify that functionality of
 * {@link edu.nyu.fc.portfolio.Portfolio} class works as expected.
 * 
 * @author Yourii Martiak
 * 
 */
public class PortfolioTest {

    /**
     * Portfolio instance under test
     */
    IPortfolio<IPosition> portfolio;

    /**
     * Setup and initialize resources needed before invocation of each test
     * method listed in this test class.
     */
    @Before
    public void setUp() {
        portfolio = new Portfolio();
    }

    /**
     * Test and verify that a new instance of portfolio successfully created as
     * a result of a previous call to setUp() method.
     */
    @Test
    public void testPortfolio() {
        assertNotNull("Default portfolio creation", portfolio);
    }

    /**
     * Test and verify that a new instance of portfolio successfully created as
     * using custom constructor with initial size.
     */
    @Test
    public void testPortfolio_ConstructorWithSize() {
        portfolio = null;
        portfolio = new Portfolio(128);
        assertNotNull("Portfolio with size creation", portfolio);
    }

    /**
     * Test and verify that a new position created and added to portfolio.
     */
    @Test
    public void testNewTrade() {
        portfolio.newTrade("IBM", 100);
        assertNotNull("New trade", portfolio);
    }

    /**
     * Test and verify that a two positions for different symbols created and
     * added to portfolio.
     */
    @Test
    public void testGetPositionIter_UniqueNewTrades() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("C", -200);
        final IPositionIter<IPosition> pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals("Checking iterator size after two unique symbol trades",
                2, size);
    }

    /**
     * Test and verify that when a two positions for the same symbol result in
     * one position being updated.
     */
    @Test
    public void testGetPositionIter_DuplicateNewTrades() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("IBM", -200);
        final IPositionIter<IPosition> pIter = portfolio.getPositionIter();
        IPosition updatedPosition = null;
        IPosition p = null;
        int size = 0;
        while((p = pIter.getNextPosition()) != null) {
            updatedPosition = p;
            size++;
        }
        assertEquals(
                "Checking iterator size after two duplicate symbol trades", 1,
                size);
        assertEquals("Checking updated position quantity", -100,
                updatedPosition.getQuantity());
    }

    /**
     * Test and verify that trading opposite sides of the same security symbol
     * and quantity will result in a zero net trade, effectively removing
     * position from portfolio.
     */
    @Test
    public void testGetPositionIter_DuplicateNewTrades_ZeroNet() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("IBM", -100);
        final IPositionIter<IPosition> pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after two duplicate symbol trades with zero net result",
                0, size);
    }

    /**
     * Test and verify that an attempt to create a new position with zero
     * quantity fails, no position added to portfolio.
     */
    @Test
    public void testGetPositionIter_InvalidNewTrade_ZeroQuantity() {
        portfolio.newTrade("IBM", 0);
        final IPositionIter<IPosition> pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after a trade with invalid quantity",
                0, size);
    }

    /**
     * Test and verify that an attempt to create a new position with null
     * symbol fails, no position added to portfolio.
     */
    @Test
    public void testGetPositionIter_InvalidNewTrade_NullSymbol() {
        portfolio.newTrade(null, 100);
        final IPositionIter<IPosition> pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after a trade with invalid symbol", 0,
                size);
    }

}
