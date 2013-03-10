package edu.nyu.fc.portfolio;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class PortfolioTest {

    IPortfolio portfolio;

    @Before
    public void setUp() {
        portfolio = new Portfolio();
    }

    @Test
    public void testPortfolio() {
        assertNotNull("Portfolio creation", portfolio);
    }

    @Test
    public void testNewTrade() {
        portfolio.newTrade("IBM", 100);
        assertNotNull("New trade", portfolio);
    }

    @Test
    public void testGetPositionIter_UniqueNewTrades() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("C", -200);
        IPositionIter pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals("Checking iterator size after two unique symbol trades",
                2, size);
    }

    @Test
    public void testGetPositionIter_DuplicateNewTrades() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("IBM", -200);
        IPositionIter pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after two duplicate symbol trades", 1,
                size);
    }

    @Test
    public void testGetPositionIter_DuplicateNewTrades_ZeroNet() {
        portfolio.newTrade("IBM", 100);
        portfolio.newTrade("IBM", -100);
        IPositionIter pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after two duplicate symbol trades with zero net result",
                0, size);
    }

    @Test
    public void testGetPositionIter_InvalidNewTrade() {
        portfolio.newTrade(null, 100);
        IPositionIter pIter = portfolio.getPositionIter();
        int size = 0;
        while (pIter.getNextPosition() != null) {
            size++;
        }
        assertEquals(
                "Checking iterator size after a trade with invalid symbol", 0,
                size);
    }

}
